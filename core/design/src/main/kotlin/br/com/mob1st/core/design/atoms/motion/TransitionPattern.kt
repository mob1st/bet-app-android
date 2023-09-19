package br.com.mob1st.core.design.atoms.motion

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Immutable
import androidx.navigation.NavBackStackEntry
import br.com.mob1st.core.design.atoms.properties.navigations.NavTarget

/**
 * The transitions pattern suggested by Material 3.
 * @see <a href="https://m3.material.io/styles/motion/transitions/transition-patterns">Transitions</a>
 */
@Immutable
internal sealed interface TransitionPattern {
    /**
     * The [EnterExitSet] for the [AnimatedContentTransitionScope.initialState] when the navigation is triggered.
     */
    val forwardNavigation: EnterExitSet

    /**
     * The [EnterExitSet] for the [AnimatedContentTransitionScope.targetState] when the back navigation is triggered.
     */
    val backNavigation: EnterExitSet
}

/**
 * Group of enter and exit animations to run when a navigation happens.
 */
interface EnterExitSet {
    fun AnimatedContentTransitionScope<NavBackStackEntry>.enter(): EnterTransition?
    fun AnimatedContentTransitionScope<NavBackStackEntry>.exit(): ExitTransition?
    fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnter(): EnterTransition? = enter()
    fun AnimatedContentTransitionScope<NavBackStackEntry>.popExit(): ExitTransition? = exit()
}

/**
 * The two actions to run in both goint to a navigation target and returning from it using the back stack.
 */
data class TransitionedNavigation(
    /**
     * The action to run when the navigation is triggered.
     */
    val goingTo: TransitionAction,

    /**
     * The action to run when the back navigation is triggered.
     */
    val returningFrom: TransitionAction,
)

/**
 * Holds the [EnterExitSet] animations to be executed when the [NavBackStackEntry.id] matches with the
 * [NavTarget.screenName].
 *
 * @param isFromSource if true, the id of [AnimatedContentTransitionScope.initialState] will be compared with the
 * [NavTarget.screenName]. Otherwise, the [AnimatedContentTransitionScope.targetState] will be compared.
 * @param target the [NavTarget] to be compared with the [NavBackStackEntry.id].
 * @param transition the [EnterExitSet] to be executed when the [NavBackStackEntry.id] matches with the
 */
data class TransitionAction(
    val isFromSource: Boolean,
    val target: NavTarget,
    val transition: EnterExitSet,
) {

    /**
     * Finds the [EnterExitSet] that matches with the [NavBackStackEntry.id] of the [AnimatedContentTransitionScope].
     */
    operator fun get(from: AnimatedContentTransitionScope<NavBackStackEntry>): EnterExitSet? {
        val id = if (isFromSource) {
            from.initialState.id
        } else {
            from.targetState.id
        }
        return if (id == target.screenName) {
            transition
        } else {
            null
        }
    }
}
