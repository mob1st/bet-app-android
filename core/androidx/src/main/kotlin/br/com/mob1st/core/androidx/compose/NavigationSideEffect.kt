package br.com.mob1st.core.androidx.compose

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
 */
@Composable
fun <T> NavigationSideEffect(
    target: T?,
    onNavigate: (T) -> Unit,
) {
    val currentNavigation by rememberUpdatedState(newValue = onNavigate)
    LaunchedEffect(target) {
        if (target != null) {
            currentNavigation(target)
        }
    }
}
