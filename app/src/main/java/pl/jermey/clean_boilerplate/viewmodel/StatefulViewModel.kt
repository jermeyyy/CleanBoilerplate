package pl.jermey.clean_boilerplate.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlin.reflect.KClass

interface State
interface Action

abstract class StatefulViewModel<STATE : State, ACTION : Action>(initialState: STATE) : ViewModel() {

    private val disposables = CompositeDisposable()

    protected val state = MutableLiveData<STATE>()

    init {
        state.postValue(initialState)
    }

    fun launch(job: () -> Disposable) {
        disposables.add(job())
    }

    fun invokeAction(action: ACTION) {
        dispatchAction(action)
    }

    protected abstract fun dispatchAction(action: ACTION)

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    inline fun <reified STATE1 : STATE, VALUE> MutableLiveData<STATE>.bind(
        noinline transformer: (state: STATE1?) -> VALUE?
    ): LiveData<VALUE> {
        return Transformations.map<STATE, VALUE>(this as LiveData<STATE>) {
            transformer(if (value is STATE1) value as STATE1 else null)
        }
    }

    fun <VALUE> MutableLiveData<STATE>.bindings(block: Binder<STATE, VALUE>.() -> FullBuilder<STATE, VALUE>) =
        Binder<STATE, VALUE>(this).let(block).build()

    inner class Binder<S : STATE, VALUE>(private val liveData: LiveData<S>) {

        val transformers: MutableMap<KClass<*>, Function1<*, VALUE>> = mutableMapOf()

        inline fun <reified STATE1 : S> bind(noinline transformer: Function1<STATE1, VALUE>) {
            if (transformers[STATE1::class] != null) throw RuntimeException("Should not override already present key")
            transformers[STATE1::class] = transformer
        }

        fun default(transformer: Function0<VALUE?>): FullBuilder<S, VALUE> =
            FullBuilder(liveData, transformers, transformer)
    }

    inner class FullBuilder<S : STATE, VALUE>(
        private val liveData: LiveData<S>,
        private val transformers: MutableMap<KClass<*>, Function1<*, VALUE>>,
        private val defaultTransformer: Function0<VALUE?>
    ) {

        @Suppress("UNCHECKED_CAST")
        fun build(): LiveData<VALUE> = Transformations
            .map<S, VALUE>(liveData) { s ->
                val transformer = transformers.getOrElse(s::class) { null } as? Function1<S, VALUE>
                transformer
                    ?.invoke(s)
                    ?: defaultTransformer.invoke()
            }
    }
}