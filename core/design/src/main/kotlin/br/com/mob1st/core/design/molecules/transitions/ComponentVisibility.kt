package br.com.mob1st.core.design.molecules.transitions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.runtime.Composable
import br.com.mob1st.core.design.atoms.motion.DurationSet
import br.com.mob1st.core.design.atoms.motion.EmphasizedEasingSet
import br.com.mob1st.core.design.atoms.motion.StandardEasingSet

data object ComponentVisibility : TransitionPattern {
    override fun enter(): EnterTransition {
        return fadeIn(
            animationSpec = tween(
                easing = EmphasizedEasingSet.decelerate,
                durationMillis = DurationSet.short.x4,
                delayMillis = DurationSet.short.x2,
            ),
        ) + expandIn(
            animationSpec = tween(
                easing = StandardEasingSet.decelerate,
                durationMillis = DurationSet.short.x4,
                delayMillis = DurationSet.short.x2,
            ),
        )
    }

    override fun exit(): ExitTransition {
        return shrinkOut() + fadeOut()
    }
}

@Composable
fun Visibility(
    isVisible: Boolean,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            spring(stiffness = Spring.StiffnessMediumLow),
        ) + expandIn(),
    ) {
        content()
    }
}
