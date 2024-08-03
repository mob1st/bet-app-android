package br.com.mob1st.core.design.molecules.loading

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Crossfade between a loading indicator and a non-loading content.
 * @param isLoading if the loading indicator should be shown.
 * @param crossfadeLabel the label for the crossfade.
 * @param nonLoadingContent the content to be shown when the loading indicator is not shown.
 */
@Composable
fun CrossfadeLoading(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    crossfadeLabel: String,
    nonLoadingContent: @Composable () -> Unit,
) {
    Crossfade(
        modifier = modifier
            .wrapContentSize()
            .padding(2.dp),
        targetState = isLoading,
        label = crossfadeLabel,
    ) { target ->
        if (target) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        } else {
            nonLoadingContent()
        }
    }
}
