package pl.jermey.clean_boilerplate.util.state

/**
 * Base interface for defining side effects in business logic
 */
interface SideEffect {

  companion object {
    /**
     * Creates side effect with provided function
     * @param block function to execute when side effect is executed
     */
    fun of(block: () -> Unit) = object : SideEffect {
      override fun execute() = block()
    }

    /**
     * Empty side effect
     */
    val empty = object : SideEffect {
      override fun execute() {
      }
    }
  }

  fun execute()
}