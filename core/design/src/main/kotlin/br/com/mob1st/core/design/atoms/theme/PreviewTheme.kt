package br.com.mob1st.core.design.atoms.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import br.com.mob1st.core.design.templates.LayoutSpec

@Composable
fun PreviewSetup(
    layoutSpec: LayoutSpec = LayoutSpec.Compact,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalLayoutSpec provides layoutSpec,
    ) {
        TwoCentsTheme {
            Box {
                content()
            }
        }
    }
}
