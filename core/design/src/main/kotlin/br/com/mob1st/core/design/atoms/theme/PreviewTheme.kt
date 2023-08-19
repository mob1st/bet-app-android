package br.com.mob1st.core.design.atoms.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier

@Composable
fun PreviewSetup(windowSizeClass: WindowWidthSizeClass, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalWindowWidthSizeClass provides windowSizeClass
    ) {
        BetTheme {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                content()
            }
        }
    }
}
