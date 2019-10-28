package pl.jermey.clean_boilerplate.util.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import pl.jermey.clean_boilerplate.util.AbstractViewModel
import pl.jermey.clean_boilerplate.util.filter
import kotlin.reflect.KClass

abstract class StatefulViewModel<STATE : State, EVENT : Event>(
  private val initialState: STATE,
  val handle: SavedStateHandle? = null
) : AbstractViewModel() {

  companion object {
    private const val STATE_KEY = "STATE_KEY"
  }

  private var stateMachine: StateMachine<STATE, EVENT>? = null

  private val _state = MutableLiveData<STATE>()
  protected val state: LiveData<STATE> = _state

  abstract val stateGraph: StateMachine.Graph<STATE, EVENT>

  init {
    val state = handle?.get<STATE>(STATE_KEY) ?: initialState
    _state.postValue(state)
  }

  fun StatefulViewModel<STATE, EVENT>.stateGraph(
    graphDefinition: StateMachine.GraphBuilder<STATE, EVENT>.() -> Unit
  ): StateMachine.Graph<STATE, EVENT> {
    val graph =
      StateMachine.GraphBuilder<STATE, EVENT>(null)
        .apply {
          initialState(handle?.get<STATE>(STATE_KEY) ?: initialState)
          graphDefinition()
        }
        .build()
    stateMachine = StateMachine.create(graph) {
      onValidTransition { transition ->
        handle?.set(STATE_KEY, transition.toState)
        if (transition.toState != _state.value) {
          _state.postValue(transition.toState)
        }
      }
    }
    return graph
  }

  fun invokeAction(action: EVENT) {
    stateMachine?.transition(action)
  }

  inline fun <reified S : STATE, VALUE> LiveData<STATE>.bindState(
    noinline transformer: (state: S?) -> VALUE?
  ): LiveData<VALUE> {
    return Transformations.map<STATE, VALUE>(this) {
      transformer(if (value is S) value as S else null)
    }
  }

  protected fun <S : Any, VALUE> LiveData<S>.bind(block: Binder<S, VALUE>.() -> BinderBuilder<S, VALUE>) =
    Binder<S, VALUE>(this).let(block).build()

  protected inner class Binder<K : Any, VALUE>(private val liveData: LiveData<K>) {

    val transformers: MutableMap<KClass<*>, Function1<*, VALUE>> = mutableMapOf()

    inline fun <reified S : K> instance(noinline transformer: Function1<S, VALUE>) {
      if (transformers[S::class] != null) throw RuntimeException("Should not override already present key")
      transformers[S::class] = transformer
    }

    fun default(transformer: Function0<VALUE?>): BinderBuilder<K, VALUE> =
      BinderBuilder(liveData, transformers, transformer)

    fun ignoreUndefined() = BinderBuilder(liveData, transformers)
  }

  protected inner class BinderBuilder<S : Any, VALUE>(
    private val liveData: LiveData<S>,
    private val transformers: Map<KClass<*>, Function1<*, VALUE>>,
    private val defaultTransformer: Function0<VALUE?>? = null
  ) {

    @Suppress("UNCHECKED_CAST")
    fun build(): LiveData<VALUE> = Transformations
      .map<S, VALUE>(liveData) { s ->
        val transformer = transformers.getOrElse(s::class) { null } as? Function1<S, VALUE>
        transformer
          ?.invoke(s)
          ?: defaultTransformer?.invoke()
      }
      .filter { (defaultTransformer == null && it != null) || defaultTransformer != null }
  }
}