package br.com.mob1st.core.design.atoms.motion

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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

/**
 * defaults enter transition for composable screens.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(
    towards: AnimatedContentTransitionScope.SlideDirection,
): EnterTransition {
    val contentTransform = slideIntoContainer(
        towards = towards,
        animationSpec = tween(
            easing = EmphasizedEasingSet.decelerate,
            durationMillis = 400,
        ),
    ) togetherWith fadeOut(animationSpec = tween(durationMillis = 200))
    return contentTransform.targetContentEnter
}

/**
 * defaults exit transition for composable screens.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(
    towards: AnimatedContentTransitionScope.SlideDirection,
): ExitTransition {
    return slideOutOfContainer(
        towards = towards,
        animationSpec = tween(
            durationMillis = 200,
            easing = EmphasizedEasingSet.accelerate,
        ),
    )
}

/**
 * [materialSharedAxisXIn] allows to switch a layout with shared X-axis enter transition.
 *
 * @param forward whether the direction of the animation is forward.
 * @param slideDistance the slide distance of the enter transition.
 * @param durationMillis the duration of the enter transition.
 */
fun materialSharedAxisXIn(
    forward: Boolean,
    slideDistance: Int,
    durationMillis: Int = MotionConstants.DEFAULT_MOTION_DURATION,
): EnterTransition = slideInHorizontally(
    animationSpec = tween(
        durationMillis = durationMillis,
        easing = FastOutSlowInEasing,
    ),
    initialOffsetX = {
        if (forward) slideDistance else -slideDistance
    },
) + fadeIn(
    animationSpec = tween(
        durationMillis = durationMillis.ForIncoming,
        delayMillis = durationMillis.ForOutgoing,
        easing = LinearOutSlowInEasing,
    ),
)

/**
 * [materialSharedAxisXOut] allows to switch a layout with shared X-axis exit transition.
 *
 * @param forward whether the direction of the animation is forward.
 * @param slideDistance the slide distance of the exit transition.
 * @param durationMillis the duration of the exit transition.
 */
fun materialSharedAxisXOut(
    forward: Boolean,
    slideDistance: Int,
    durationMillis: Int = MotionConstants.DEFAULT_MOTION_DURATION,
): ExitTransition = slideOutHorizontally(
    animationSpec = tween(
        durationMillis = durationMillis,
        easing = FastOutSlowInEasing,
    ),
    targetOffsetX = {
        if (forward) -slideDistance else slideDistance
    },
) + fadeOut(
    animationSpec = tween(
        durationMillis = durationMillis.ForOutgoing,
        delayMillis = 0,
        easing = FastOutLinearInEasing,
    ),
)

object MotionConstants {
    const val DEFAULT_MOTION_DURATION: Int = 300
    val DefaultSlideDistance: Dp = 30.dp
}

private const val PROGRESS_THRESHOLD = 0.35f

private val Int.ForOutgoing: Int
    get() = (this * PROGRESS_THRESHOLD).toInt()

private val Int.ForIncoming: Int
    get() = this - this.ForOutgoing

@Composable
fun rememberSlideDistance(
    slideDistance: Dp = MotionConstants.DefaultSlideDistance,
): Int {
    val density = LocalDensity.current
    return remember(density, slideDistance) {
        with(density) { slideDistance.roundToPx() }
    }
}
