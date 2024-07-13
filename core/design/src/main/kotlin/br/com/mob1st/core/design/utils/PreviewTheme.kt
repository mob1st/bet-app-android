package br.com.mob1st.core.design.utils

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import br.com.mob1st.core.design.atoms.theme.TwoCentsTheme

/**
 * A simple wrapper around [TwoCentsTheme] to be used in previews, applying the proper background color.
 * @param content The content to be displayed in the preview.
 */
@Composable
fun PreviewTheme(
    content: @Composable () -> Unit,
) {
    TwoCentsTheme {
        Surface {
            content()
        }
    }
}
