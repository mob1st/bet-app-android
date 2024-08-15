package br.com.mob1st.core.design.molecules.transitions

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import br.com.mob1st.core.design.atoms.motion.EmphasizedEasingSet
import br.com.mob1st.core.design.atoms.motion.StandardEasingSet
import kotlin.math.E
import kotlin.math.PI

/**
 * Back and forward transitions for composable screens.
 * @see [https://m3.material.io/styles/motion/transitions/transition-patterns#df9c7d76-1454-47f3-ad1c-268a31f58bad]
 */
internal class BackAndForward(
    private val scope: AnimatedContentTransitionScope<*>,
    private val towards: SlideDirection,
) : TransitionPattern {
    override fun enter(): EnterTransition = with(scope) {
        val durationForTransition = DurationForTransition.Enter
        slideIntoContainer(
            towards = towards,
            animationSpec = tween(
                easing = EmphasizedEasingSet.decelerate,
                durationMillis = durationForTransition.actual,
            ),
            initialOffset = ::offset,
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = durationForTransition.incoming,
                delayMillis = durationForTransition.outgoing,
                easing = StandardEasingSet.decelerate,
            ),
        )
    }

    override fun exit(): ExitTransition = with(scope) {
        val durationForTransition = DurationForTransition.Exit
        slideOutOfContainer(
            towards = towards,
            animationSpec = tween(
                durationMillis = durationForTransition.actual,
                easing = EmphasizedEasingSet.accelerate,
            ),
            targetOffset = ::offset,
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = durationForTransition.outgoing,
                delayMillis = 0,
                easing = StandardEasingSet.accelerate,
            ),
        )
    }

    private fun offset(value: Int): Int = (value * PI / E).toInt()
}
