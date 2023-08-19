package br.com.mob1st.core.design.atoms.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun BetTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        content = content
    )
}
