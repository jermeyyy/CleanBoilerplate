package pl.jermey.clean_boilerplate.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import pl.jermey.clean_boilerplate.util.StateMachine
import kotlin.reflect.KClass

interface State
interface Event

@Suppress("unused")
abstract class StatefulViewModel<STATE : State, EVENT : Event>(private val initialState: STATE) : ViewModel() {

    private var stateMachine: StateMachine<STATE, EVENT, Nothing>? = null

    private val disposables = CompositeDisposable()

    protected val state = MutableLiveData<STATE>()

    abstract val stateGraph: StateMachine.Graph<STATE, EVENT, Nothing>

    init {
        state.postValue(initialState)
    }

    fun StatefulViewModel<STATE, EVENT>.stateGraph(
        init: StateMachine.GraphBuilder<STATE, EVENT, Nothing>.() -> Unit
    ): StateMachine.Graph<STATE, EVENT, Nothing> {
        val graph =
            StateMachine.GraphBuilder<STATE, EVENT, Nothing>(null)
                .apply { initialState(initialState) }
                .apply(init)
                .build()
        stateMachine = StateMachine.create(graph) {
            onValidTransition { transition -> state.postValue(transition.toState) }
        }
        return graph
    }

    fun launch(job: () -> Disposable) {
        disposables.add(job())
    }

    fun invokeAction(action: EVENT) {
        stateMachine?.transition(action)
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    inline fun <reified STATE1 : STATE, VALUE> MutableLiveData<STATE>.bindState(
        noinline transformer: (state: STATE1?) -> VALUE?
    ): LiveData<VALUE> {
        return Transformations.map<STATE, VALUE>(this as LiveData<STATE>) {
            transformer(if (value is STATE1) value as STATE1 else null)
        }
    }

    fun <VALUE> MutableLiveData<STATE>.bind(block: Binder<STATE, VALUE>.() -> BinderBuilder<STATE, VALUE>) =
        Binder<STATE, VALUE>(this).let(block).build()

    inner class Binder<BINDING_STATE : STATE, VALUE>(private val liveData: LiveData<BINDING_STATE>) {

        val transformers: MutableMap<KClass<*>, Function1<*, VALUE>> = mutableMapOf()

        inline fun <reified S : BINDING_STATE> Binder<BINDING_STATE, VALUE>.state(noinline transformer: Function1<S, VALUE>) {
            if (transformers[S::class] != null) throw RuntimeException("Should not override already present key")
            transformers[S::class] = transformer
        }

        fun Binder<BINDING_STATE, VALUE>.default(transformer: Function0<VALUE?>): BinderBuilder<BINDING_STATE, VALUE> =
            BinderBuilder(liveData, transformers, transformer)
    }

    inner class BinderBuilder<S : STATE, VALUE>(
        private val liveData: LiveData<S>,
        private val transformers: MutableMap<KClass<*>, Function1<*, VALUE>>,
        private val defaultTransformer: Function0<VALUE?>
    ) {

        @Suppress("UNCHECKED_CAST")
        fun build(): LiveData<VALUE> = Transformations
            .map<S, VALUE>(liveData) { s ->
                val transformer = transformers.getOrElse(s::class) { null } as? Function1<S, VALUE>
                transformer?.invoke(s) ?: defaultTransformer.invoke()
            }
    }
}