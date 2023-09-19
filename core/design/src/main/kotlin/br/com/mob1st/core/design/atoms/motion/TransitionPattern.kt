package br.com.mob1st.core.design.atoms.motion

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavBackStackEntry

/**
 * Group of enter and exit animations to run when a navigation happens.
 *
 * If no transition is required then returns null. In this case the default navigation for the Nav graph will be used.
 * When implementing each of its methods, always ensure the [AnimatedContentTransitionScope.initialState] and
 * [AnimatedContentTransitionScope.targetState] matches the targets of the transition to avoid run the wrong transition
 * animation for a screen. The [NavigationMatches] can be useful for that.
 * @see <a href="https://m3.material.io/styles/motion/transitions/transition-patterns">Transition Patterns</a>
 * @see [NavigationMatches]
 */
sealed interface TransitionPattern {

    /**
     * The enter animation to run when a composable enters the screen.
     *
     * @return [EnterTransition] or null
     */
    fun AnimatedContentTransitionScope<NavBackStackEntry>.enter(): EnterTransition?

    /**
     * The exit animation to run when a composable exits the screen.
     *
     * @return [ExitTransition] or null
     */
    fun AnimatedContentTransitionScope<NavBackStackEntry>.exit(): ExitTransition?

    /**
     * The enter animation to run when a composable enters the screen after a back navigation. Defaults to [enter].
     *
     * @return [EnterTransition] or null
     */
    fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnter(): EnterTransition? = enter()

    /**
     * The exit animation to run when a composable exits the screen after a back navigation. Defaults to [exit].
     *
     * @return [ExitTransition] or null
     */
    fun AnimatedContentTransitionScope<NavBackStackEntry>.popExit(): ExitTransition? = exit()
}

/**
 * Get the first enter transition that is not null.
 */
internal fun List<TransitionPattern>.firstNotNullEnter(
    scoped: TransitionPattern.() -> EnterTransition?,
): EnterTransition? = firstNotNullOfOrNull { it.scoped() }

/**
 * Get the first exit transition that is not null.
 */
internal fun List<TransitionPattern>.firstNotNullExit(
    scoped: TransitionPattern.() -> ExitTransition?,
): ExitTransition? = firstNotNullOfOrNull { it.scoped() }
