package pl.jermey.clean_boilerplate.util.viewmodel

import androidx.lifecycle.*
import kotlin.reflect.KClass

abstract class StatefulViewModel<STATE : State, EVENT : Event>(
  private val initialState: STATE,
  private val savedState: SavedStateHandle
) : AbstractViewModel() {

  companion object {
    private const val STATE_KEY = "savedState"
  }

  private var stateMachine: StateMachine<STATE, EVENT>? = null

  private val _state = MutableLiveData<STATE>()
  protected val state: LiveData<STATE> = _state

  abstract val stateGraph: StateMachine.Graph<STATE, EVENT>

  init {
    if (savedState.contains(STATE_KEY)) {
      _state.postValue(savedState.get(STATE_KEY))
    } else {
      _state.postValue(initialState)
    }
  }

  fun StatefulViewModel<STATE, EVENT>.stateGraph(
    init: StateMachine.GraphBuilder<STATE, EVENT>.() -> Unit
  ): StateMachine.Graph<STATE, EVENT> {
    val graph =
      StateMachine.GraphBuilder<STATE, EVENT>(null)
        .apply { initialState(initialState) }
        .apply(init)
        .build()
    stateMachine = StateMachine.create(graph) {
      onValidTransition { transition ->
        savedState.set(STATE_KEY, transition.toState)
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

  inline fun <reified STATE1 : STATE, VALUE> LiveData<STATE>.bindState(
    noinline transformer: (state: STATE1?) -> VALUE?
  ): LiveData<VALUE> {
    return Transformations.map<STATE, VALUE>(this) {
      transformer(if (value is STATE1) value as STATE1 else null)
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

  private fun <T> LiveData<T>.filter(predicate: (T) -> Boolean): LiveData<T> {
    val mediator = MediatorLiveData<T>()
    mediator.addSource(this) { t -> if (predicate(t)) mediator.postValue(t) }
    return mediator
  }
}