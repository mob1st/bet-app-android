package br.com.mob1st.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState

/**
 * Launches navigation when the target is not null.
 * It handles side effect navigation consuming the navigation state after it is triggered.
 *
 * @param target The target to navigate to.
 * @param onNavigate The navigation function.
 * @param onConsumeNavigation The function to consume the navigation state.
 */
@Composable
fun <T : NavTarget> SideEffectNavigation(target: T?, onNavigate: (T) -> Unit, onConsumeNavigation: () -> Unit) {
    val currentNavigation by rememberUpdatedState(newValue = onNavigate)
    val currentConsumeNavigation by rememberUpdatedState(newValue = onConsumeNavigation)
    LaunchedEffect(target) {
        if (target != null) {
            currentNavigation(target)
            currentConsumeNavigation()
        }
    }
}
