package br.com.mob1st.features.finances.impl.ui.builder.completion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.com.mob1st.core.design.utils.PreviewTheme
import br.com.mob1st.core.design.utils.ThemedPreview

@Composable
fun BuilderCompletionPage(
    onComplete: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Button(onClick = onComplete) {
            Text(text = "Finished")
        }
    }
}

@Composable
@ThemedPreview
private fun BuilderCompletionPagePreview() {
    PreviewTheme {
        BuilderCompletionPage {}
    }
}
