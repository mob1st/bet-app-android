package br.com.mob1st.features.finances.impl.ui.builder.completion

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import br.com.mob1st.core.design.utils.ThemedPreview

@Composable
fun BuilderCompletionPage(
    onComplete: () -> Unit,
) {
    Text(text = "Sample")
}

@Composable
@ThemedPreview
private fun BuilderCompletionPagePreview() {
    BuilderCompletionPage {}
}
