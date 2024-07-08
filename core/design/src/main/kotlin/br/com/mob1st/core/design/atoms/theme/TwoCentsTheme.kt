package br.com.mob1st.core.design.atoms.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import br.com.mob1st.core.design.atoms.colors.ColorSchemeFactory
import br.com.mob1st.core.design.atoms.typography.TypographyFactory

/**
 * Base theme style for the app.
 * The role of this theme is setup the [MaterialTheme] parameters.
 * This function does not start LocalProviders or does any setup for the app, which make it useful for @Preview
 * functions.
 * @param content The content to be displayed inside the theme.
 */
@Composable
fun TwoCentsTheme(content: @Composable () -> Unit) {
    val isDark = isSystemInDarkTheme()
    val colorScheme = remember(isDark) {
        ColorSchemeFactory.create(isDark)
    }
    val typography = remember {
        TypographyFactory.create()
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content,
    )
}