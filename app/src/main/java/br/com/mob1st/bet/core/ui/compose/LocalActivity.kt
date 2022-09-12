package br.com.mob1st.bet.core.ui.compose

import androidx.activity.ComponentActivity
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Provides the instance of the current activity to the composable tree.
 *
 * It have to be called by the current activity on screen to works, otherwise it will trigger a
 * IllegalStateException
 */
val LocalActivity = staticCompositionLocalOf<ComponentActivity> {
    error("CompositionLocal for LocalActivity not present")
}