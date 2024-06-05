package br.com.mob1st.core.design.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Annotation to be used os a [Preview] to show the screen in both light and dark mode.
 */
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
annotation class ThemedPreview

@Preview
annotation class WindowLayoutPreview
