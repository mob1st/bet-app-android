package br.com.mob1st.core.design.molecules.loading

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import br.com.mob1st.core.design.atoms.motion.DurationSet
import br.com.mob1st.core.design.atoms.motion.StandardEasingSet

/**
 * Crossfade between a loading indicator and a non-loading content.
 * @param isLoading if the loading indicator should be shown.
 * @param nonLoadingContent the content to be shown when the loading indicator is not shown.
 */
@Composable
fun CrossfadeLoading(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    nonLoadingContent: @Composable () -> Unit,
) {
    val state = remember(isLoading) { CrossfadeState.create(isLoading) }
    val slotAlpha by state.slotConfig.animate()
    val progressAlpha by state.progressConfig.animate()
    Box(modifier = modifier) {
        Box(modifier = Modifier.alpha(slotAlpha)) {
            nonLoadingContent()
        }
        CircularProgressIndicator(
            modifier = Modifier
                .wrapContentSize()
                .alpha(progressAlpha),
            strokeWidth = CrossfadeLoadingDefaults.progressWidth,
            color = LocalContentColor.current,
        )
    }
}

private data class CrossfadeState(
    val progressConfig: CrossfadeConfig,
    val slotConfig: CrossfadeConfig,
) {
    companion object {
        fun create(isLoading: Boolean): CrossfadeState {
            return if (isLoading) {
                CrossfadeState(
                    progressConfig = CrossfadeConfig.visible("progress_crossfade"),
                    slotConfig = CrossfadeConfig.invisible("slot_crossfade"),
                )
            } else {
                CrossfadeState(
                    progressConfig = CrossfadeConfig.invisible("progress_crossfade"),
                    slotConfig = CrossfadeConfig.visible("slot_crossfade"),
                )
            }
        }
    }
}

private data class CrossfadeConfig(
    val progressAlpha: Float,
    val duration: Int,
    val easing: Easing,
    val label: String,
    val delay: Int,
) {
    @Composable
    fun animate() = animateFloatAsState(
        targetValue = progressAlpha,
        label = label,
        animationSpec = tween(
            durationMillis = duration,
            delayMillis = delay,
            easing = easing,
        ),
    )

    companion object {
        private val enterCombination = DurationSet.short.x4 to StandardEasingSet.decelerate
        private val exitCombination = DurationSet.short.x2 to StandardEasingSet.accelerate

        fun visible(label: String): CrossfadeConfig {
            return CrossfadeConfig(
                progressAlpha = 1f,
                duration = enterCombination.first,
                easing = enterCombination.second,
                delay = exitCombination.first,
                label = label,
            )
        }

        fun invisible(label: String): CrossfadeConfig {
            return CrossfadeConfig(
                progressAlpha = 0f,
                duration = exitCombination.first,
                easing = exitCombination.second,
                delay = 0,
                label = label,
            )
        }
    }
}

object CrossfadeLoadingDefaults {
    val progressWidth = 2.dp
}
