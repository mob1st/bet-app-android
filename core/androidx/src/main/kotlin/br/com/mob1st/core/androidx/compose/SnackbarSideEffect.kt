package br.com.mob1st.core.androidx.compose

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.launch

/**
 * Triggers the display of a snackbar in a side effect safely way.
 * @param snackbarHostState The snackbar host state.
 * @param snackbarVisuals The data to be displayed in the snackbar.
 * @param onDismiss Callback to be called when the snackbar is dismissed.
 * @param onPerformAction Callback to be called when the snackbar action is performed.
 */
@Composable
fun SnackbarSideEffect(
    snackbarHostState: SnackbarHostState,
    snackbarVisuals: SnackbarVisuals?,
    onDismiss: () -> Unit,
    onPerformAction: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val currentOnDismiss by rememberUpdatedState(newValue = onDismiss)
    val currentOnPerformAction by rememberUpdatedState(newValue = onPerformAction)
    LaunchedEffect(snackbarHostState, snackbarVisuals) {
        snackbarVisuals?.let {
            scope.launch {
                val result = snackbarHostState.showSnackbar(snackbarVisuals)
                when (result) {
                    SnackbarResult.Dismissed -> currentOnDismiss()
                    SnackbarResult.ActionPerformed -> currentOnPerformAction()
                }
            }
        }
    }
}
