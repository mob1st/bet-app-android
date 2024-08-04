package br.com.mob1st.core.design.molecules.transitions

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import br.com.mob1st.core.design.atoms.motion.EmphasizedEasingSet
import br.com.mob1st.core.design.atoms.motion.StandardEasingSet

/**
 * This pattern is used to navigate between top-level destinations of an app, like tapping a destination in a
 * Navigation bar.
 * @see [https://m3.material.io/styles/motion/transitions/transition-patterns#8327d206-2a7d-423a-abf0-2be86130535b]
 */
data object TopLevel : TransitionPattern {
    private const val SCALE = 0.90f

    override fun enter(): EnterTransition {
        val duration = DurationForTransition.Enter
        return fadeIn(
            animationSpec = tween(
                durationMillis = duration.incoming,
                delayMillis = duration.outgoing,
                easing = EmphasizedEasingSet.decelerate,
            ),
        ) + scaleIn(
            animationSpec = tween(
                durationMillis = duration.incoming,
                delayMillis = duration.outgoing,
                easing = StandardEasingSet.decelerate,
            ),
            initialScale = SCALE,
        )
    }

    override fun exit(): ExitTransition {
        val durationMillis = DurationForTransition.Exit
        return fadeOut(
            animationSpec = tween(
                durationMillis = durationMillis.outgoing,
                delayMillis = 0,
                easing = EmphasizedEasingSet.accelerate,
            ),
        )
    }
}
