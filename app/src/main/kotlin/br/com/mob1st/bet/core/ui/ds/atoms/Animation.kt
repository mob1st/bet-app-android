package br.com.mob1st.bet.core.ui.ds.atoms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable

@Composable
fun <T> ReplaceAnimation(
    targetState: T,
    content: @Composable (current: T) -> Unit,
) {
    Crossfade(
        targetState = targetState,
    ) { current ->
        content(current)
    }
}

@Composable
fun VisibilityAnimation(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        content = content,
    )
}
