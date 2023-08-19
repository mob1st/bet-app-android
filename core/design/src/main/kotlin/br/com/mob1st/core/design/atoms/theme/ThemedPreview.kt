package br.com.mob1st.core.design.atoms.theme

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Annotation to be used os a [Preview] to show the screen in both light and dark mode.
 */
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
annotation class ThemedPreview
