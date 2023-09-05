package br.com.mob1st.core.design.molecules.loading

import androidx.compose.animation.Crossfade
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun Loading(isLoading: Boolean, crossfadeLabel: String, nonLoadingContent: @Composable () -> Unit) {
    Crossfade(isLoading, label = crossfadeLabel) { target ->
        if (target) {
            CircularProgressIndicator(
                strokeWidth = 2.dp
            )
        } else {
            nonLoadingContent()
        }
    }
}
