package br.com.mob1st.core.design.atoms.motion

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry
import kotlin.math.E

/**
 * defaults enter transition for composable screens.
 */
fun AnimatedContentTransitionScope<*>.enterTransition(
    towards: AnimatedContentTransitionScope.SlideDirection,
): EnterTransition {
    val duration = 400
    return slideIntoContainer(
        towards = towards,
        animationSpec = tween(
            easing = EmphasizedEasingSet.decelerate,
            durationMillis = duration,
        ),
        initialOffset = {
            (it * E).toInt()
        },
    ) + fadeIn(
        animationSpec = tween(
            // durationMillis = duration.ForIncoming,
            // delayMillis = duration.ForOutgoing,
            easing = StandardEasingSet.decelerate,
        ),
    )
}

/**
 * defaults exit transition for composable screens.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(
    towards: AnimatedContentTransitionScope.SlideDirection,
): ExitTransition {
    val duration = 200
    return slideOutOfContainer(
        towards = towards,
        animationSpec = tween(
            durationMillis = duration,
            easing = EmphasizedEasingSet.accelerate,
        ),
        targetOffset = {
            (it * E).toInt()
        },
    ) + fadeOut(
        animationSpec = tween(
            // durationMillis = duration.ForOutgoing,
            easing = StandardEasingSet.accelerate,
        ),
    )
}
