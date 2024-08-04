package br.com.mob1st.core.design.motion

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.mob1st.core.design.atoms.motion.DurationSet
import br.com.mob1st.core.design.atoms.motion.EmphasizedEasingSet
import br.com.mob1st.core.design.atoms.motion.StandardEasingSet

/**
 * Composable navigation destination that slides in horizontally.
 */
inline fun <reified T : Any> NavGraphBuilder.slide(
    route: String,
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) = composable(
    route = route,
    deepLinks = deepLinks,
    enterTransition = {
        slideInHorizontally(towards = AnimatedContentTransitionScope.SlideDirection.Start)
    },
    exitTransition = {
        slideOutHorizontally(towards = AnimatedContentTransitionScope.SlideDirection.Start)
    },
    popEnterTransition = {
        slideInHorizontally(towards = AnimatedContentTransitionScope.SlideDirection.End)
    },
    popExitTransition = {
        slideOutHorizontally(towards = AnimatedContentTransitionScope.SlideDirection.End)
    },
    content = content,
)

/**
 * Slide in horizontally from the given direction.
 * @param towards The direction to slide in.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideInHorizontally(
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
 * Slide out horizontally to the given direction.
 * @param towards The direction to slide out.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOutHorizontally(
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
