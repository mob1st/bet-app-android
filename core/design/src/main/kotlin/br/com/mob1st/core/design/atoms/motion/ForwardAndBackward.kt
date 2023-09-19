package br.com.mob1st.core.design.atoms.motion

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry
import br.com.mob1st.core.design.atoms.properties.navigations.NavTarget

/**
 * Implements the Forward and Backward [TransitionPattern] from Material 3.
 *
 * @see <a href="https://m3.material.io/styles/motion/transitions/transition-patterns">Transitions</a>
 */
data class ForwardAndBackward(
    val first: NavTarget,
    val second: NavTarget,
    val slideOrientation: SlideOrientation = SlideOrientation.Horizontal,
) : TransitionPattern {

    private val forwardNavigationMatches = NavigationMatches(first, second)
    private val backwardNavigationMatches = NavigationMatches(second, first)

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enter(): EnterTransition? {
        return if (forwardNavigationMatches(initialState, targetState)) {
            enter(towards = slideOrientation.forwardDirection)
        } else {
            null
        }
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exit(): ExitTransition? {
        return if (forwardNavigationMatches(initialState, targetState)) {
            exit(towards = slideOrientation.forwardDirection)
        } else {
            null
        }
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnter(): EnterTransition? {
        return if (backwardNavigationMatches(initialState, targetState)) {
            enter(towards = slideOrientation.backwardDirection)
        } else {
            null
        }
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExit(): ExitTransition? {
        return if (backwardNavigationMatches(initialState, targetState)) {
            exit(towards = slideOrientation.backwardDirection)
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

    /**
     * The direction of the transition.
     */
    sealed interface SlideOrientation {

        /**
         * The direction of the transition when navigating forward.
         */
        val forwardDirection: AnimatedContentTransitionScope.SlideDirection

        /**
         * The direction of the transition when navigating backward.
         */
        val backwardDirection: AnimatedContentTransitionScope.SlideDirection

        /**
         * The transition direction is vertical.
         *
         * It moves down when navigating forward and up when navigating backward.
         */
        data object Vertical : SlideOrientation {
            override val forwardDirection = AnimatedContentTransitionScope.SlideDirection.Down
            override val backwardDirection = AnimatedContentTransitionScope.SlideDirection.Up
        }

        /**
         * The transition direction is horizontal.
         *
         * It moves left when navigating forward and right when navigating backward.
         */
        data object Horizontal : SlideOrientation {
            override val forwardDirection = AnimatedContentTransitionScope.SlideDirection.Left
            override val backwardDirection = AnimatedContentTransitionScope.SlideDirection.Right
        }
    }
}
