package br.com.mob1st.core.design.molecules.transitions

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import br.com.mob1st.core.design.atoms.motion.EmphasizedEasingSet
import br.com.mob1st.core.design.atoms.motion.StandardEasingSet

data object TopLevel : NavTransitionPattern {
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
