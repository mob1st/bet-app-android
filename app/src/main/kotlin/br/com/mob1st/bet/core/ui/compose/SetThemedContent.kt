package br.com.mob1st.bet.core.ui.compose

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import br.com.mob1st.bet.core.ui.ds.atoms.BetTheme

/**
 * Wraps the given [content] into the app theme and also do the setups of the Locals available to
 * all screen tree
 *
 * Uses the [systemBars] function to customize the color of
 */
fun ComponentActivity.setThemedContent(content: @Composable () -> Unit) {
    setContent {
        CompositionLocalProvider(
            LocalActivity provides this,
        ) {
            BetTheme {
                content()
            }
        }
    }
}
