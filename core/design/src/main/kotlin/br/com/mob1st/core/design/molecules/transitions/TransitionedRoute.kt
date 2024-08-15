package br.com.mob1st.core.design.molecules.transitions

import kotlinx.serialization.Serializable

/**
 * A type of navigation route that uses the default transition parameters to define the transition pattern to be
 * applied.
 * @see transitioned
 */
interface TransitionedRoute {
    /**
     * The entering navigation pattern to be applied when this route enters the screen.
     */
    val enteringPatternKey: PatternKey?
}

/**
 * The keys that can be used to define the navigation pattern to be applied when a route enters the screen.
 */
@Serializable
enum class PatternKey {
    BackAndForward,
    TopLevel,
}
