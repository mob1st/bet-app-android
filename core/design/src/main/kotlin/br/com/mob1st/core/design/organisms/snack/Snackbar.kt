package br.com.mob1st.core.design.organisms.snack

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.launch

@Composable
fun Snackbar(
    snackbarHostState: SnackbarHostState,
    snackbarVisuals: SnackbarVisuals?,
    onDismiss: () -> Unit,
    onPerformAction: () -> Unit,
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

fun snackbar(
    message: String,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
    withDismissAction: Boolean = false,
): SnackbarVisuals =
    SnackbarVisualsImpl(
        message = message,
        actionLabel = actionLabel,
        duration = duration,
        withDismissAction = withDismissAction,
    )

@Immutable
private data class SnackbarVisualsImpl(
    override val message: String,
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val withDismissAction: Boolean = false,
) : SnackbarVisuals
