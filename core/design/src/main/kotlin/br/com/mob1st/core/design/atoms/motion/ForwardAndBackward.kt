package br.com.mob1st.core.design.atoms.motion

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Immutable
import androidx.navigation.NavBackStackEntry
import br.com.mob1st.core.design.atoms.properties.navigations.NavTarget

/**
 * Implements the Forward and Backward [TransitionPattern] from Material 3.
 * @see <a href="https://m3.material.io/styles/motion/transitions/transition-patterns#df9c7d76-1454-47f3-ad1c-268a31f58bad">
 *     Transitions
 *     </a>
 */
@Immutable
data class ForwardAndBackward(
    val first: NavTarget,
    val second: NavTarget,
) : EnterExitSet {

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enter(): EnterTransition? {
        return if (first matches initialState && second matches targetState) {
            enter(towards = AnimatedContentTransitionScope.SlideDirection.Left)
        } else {
            null
        }
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exit(): ExitTransition? {
        return if (first matches initialState && second matches targetState) {
            exit(towards = AnimatedContentTransitionScope.SlideDirection.Left)
        } else {
            null
        }
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnter(): EnterTransition? {
        return if (first matches targetState && second matches initialState) {
            enter(towards = AnimatedContentTransitionScope.SlideDirection.Right)
        } else {
            null
        }
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExit(): ExitTransition? {
        return if (first matches targetState && second matches initialState) {
            exit(towards = AnimatedContentTransitionScope.SlideDirection.Right)
        } else {
            null
        }
    }

    companion object {
        private fun AnimatedContentTransitionScope<NavBackStackEntry>.enter(
            towards: AnimatedContentTransitionScope.SlideDirection,
        ): EnterTransition {
            return slideIntoContainer(
                towards = towards,
                animationSpec = tween(
                    durationMillis = Duration.long2,
                    easing = EmphasizedEasingSet.default
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = Duration.medium2,
                    delayMillis = Duration.short4,
                    easing = StandardEasingSet.decelerate
                )
            )
        }

        private fun AnimatedContentTransitionScope<NavBackStackEntry>.exit(
            towards: AnimatedContentTransitionScope.SlideDirection,
        ): ExitTransition {
            return slideOutOfContainer(
                towards = towards,
                animationSpec = tween(Duration.long2, easing = EmphasizedEasingSet.default)
            ) + fadeOut(
                animationSpec = tween(
                    durationMillis = Duration.short4,
                    easing = StandardEasingSet.accelerate
                )
            )
        }
    }
}

infix fun NavTarget.matches(entry: NavBackStackEntry): Boolean {
    return screenName == entry.destination.route
}
