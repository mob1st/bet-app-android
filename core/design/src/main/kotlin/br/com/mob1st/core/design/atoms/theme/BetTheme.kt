package br.com.mob1st.core.design.atoms.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import br.com.mob1st.core.design.atoms.colors.AppSchemeFactory

@Composable
fun BetTheme(content: @Composable () -> Unit) {
    val isDark = isSystemInDarkTheme()
    val colorScheme = remember(isDark) {
        AppSchemeFactory.create(isDark)
    }
    MaterialTheme(
        content = content,
        colorScheme = colorScheme
    )
}
