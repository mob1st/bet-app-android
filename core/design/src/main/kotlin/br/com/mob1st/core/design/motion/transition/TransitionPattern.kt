package br.com.mob1st.core.design.motion.transition

import android.os.Parcelable
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry
import kotlinx.parcelize.Parcelize

/**
 * A pattern of transitions for a navigation destination.
 * It applies the Material Design guidelines for transitions.
 * @see [Material Design - Transition Patterns](https://m3.material.io/styles/motion/transitions/transition-patterns)
 */
sealed interface TransitionPattern : Parcelable {
    /**
     * The enter transition to run when a composable enters the screen.
     * It's called when the composable is added to the screen.
     */

    fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition

    /**
     * The exit transition to run when a composable exits the screen.
     * It's called when the composable is removed from the screen.
     */
    fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition

    /**
     * The enter transition to run when a composable enters the screen after a back navigation.
     * It's called when the composable is added to the screen after a back navigation.
     */
    fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition

    /**
     * The exit transition to run when a composable exits the screen after a back navigation.
     * It's called when the composable is removed from the screen after a back navigation.
     */
    fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition(): ExitTransition

    @Parcelize
    data object Slide : TransitionPattern {
        override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition {
            return slideIn(towards = AnimatedContentTransitionScope.SlideDirection.Start)
        }

        override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition {
            return slideOut(towards = AnimatedContentTransitionScope.SlideDirection.Start)
        }

        override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition {
            return slideIn(towards = AnimatedContentTransitionScope.SlideDirection.End)
        }

        override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition(): ExitTransition {
            return slideOut(towards = AnimatedContentTransitionScope.SlideDirection.End)
        }
    }

    @Parcelize
    data object TopLevel : TransitionPattern {
        override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition {
            return fadeIn()
        }

        override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition {
            return fadeOut()
        }

        override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition {
            return fadeIn()
        }

        override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition(): ExitTransition {
            return fadeOut()
        }
    }
}
