package br.com.mob1st.core.design.molecules.transitions

/**
 * A navigation entry point that can be triggered to create a composable screen.
 * It defines the entering navigation pattern to be applied when this route enters the screen.
 * @see route
 */
interface NavRoute {
    /**
     * The entering navigation pattern to be applied when this route enters the screen.
     */
    val enteringPatternKey: PatternKey
}

/**
 * The keys that can be used to define the navigation pattern to be applied when a route enters the screen.
 */
enum class PatternKey {
    BackAndForward,
    TopLevel,
}
