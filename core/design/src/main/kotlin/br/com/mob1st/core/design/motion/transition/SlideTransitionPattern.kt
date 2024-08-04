package br.com.mob1st.core.design.motion.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry
import br.com.mob1st.core.design.atoms.motion.DurationSet
import br.com.mob1st.core.design.atoms.motion.EmphasizedEasingSet
import br.com.mob1st.core.design.atoms.motion.StandardEasingSet

/**
 * Slide the content in from the given direction.
 * It can be used to slide in horizontally in the enter or pop enter transitions.
 */
internal fun AnimatedContentTransitionScope<NavBackStackEntry>.slideIn(
    towards: AnimatedContentTransitionScope.SlideDirection,
): EnterTransition {
    return slideIntoContainer(
        towards = towards,
        animationSpec = tween(
            easing = EmphasizedEasingSet.decelerate,
            durationMillis = DurationSet.medium.x4,
        ),
    ) + fadeIn(
        animationSpec = tween(
            durationMillis = DurationSet.medium.x1,
            delayMillis = DurationSet.short.x2,
            easing = StandardEasingSet.decelerate,
        ),
    )
}

/**
 * Slide the content out to the given direction.
 * It can be used to slide out horizontally in the exit or pop exit transitions.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOut(
    towards: AnimatedContentTransitionScope.SlideDirection,
): ExitTransition {
    return slideOutOfContainer(
        towards = towards,
        animationSpec = tween(
            durationMillis = DurationSet.short.x4,
            easing = EmphasizedEasingSet.accelerate,
        ),
    ) + fadeOut(
        animationSpec = tween(
            durationMillis = DurationSet.short.x4,
            delayMillis = DurationSet.short.x1,
            easing = StandardEasingSet.accelerate,
        ),
    )
}
