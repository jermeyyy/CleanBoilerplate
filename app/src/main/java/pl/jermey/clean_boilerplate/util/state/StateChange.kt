package pl.jermey.clean_boilerplate.util.state

/**
 * Wrapper class for state transition with optional side effect
 * @param state newState
 * @param sideEffect optional side effect
 */
data class StateChange<S>(
  val state: S,
  val sideEffect: SideEffect = SideEffect.empty
)

infix fun <S> S.with(sideEffect: SideEffect): StateChange<S> = StateChange(this, sideEffect)