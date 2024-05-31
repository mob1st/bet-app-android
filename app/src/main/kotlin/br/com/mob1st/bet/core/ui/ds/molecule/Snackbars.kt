package br.com.mob1st.bet.core.ui.ds.molecule

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import br.com.mob1st.bet.R
import br.com.mob1st.bet.core.ui.compose.LocalSnackbarState

@Composable
fun SnackBar(
    state: SnackState?,
    snackbarHostState: SnackbarHostState = LocalSnackbarState.current,
    onDismiss: (state: SnackState) -> Unit,
    onActionPerformed: (state: SnackState) -> Unit,
) {
    state ?: return
    val currentOnDismiss by rememberUpdatedState(onDismiss)
    val currentOnActionPerformed by rememberUpdatedState(onActionPerformed)
    val resources = LocalContext.current.resources
    LaunchedEffect(snackbarHostState, state) {
        val result =
            snackbarHostState.showSnackbar(
                message = state.message.resolve(resources),
            )
        when (result) {
            SnackbarResult.Dismissed -> currentOnDismiss(state)
            SnackbarResult.ActionPerformed -> currentOnActionPerformed(state)
        }
    }
}

@Composable
fun DismissSnackbar(
    message: String,
    snackbarHostState: SnackbarHostState = LocalSnackbarState.current,
    onDismiss: () -> Unit,
) {
    val onDismissUpdated by rememberUpdatedState(onDismiss)
    LaunchedEffect(snackbarHostState, message) {
        snackbarHostState.showDismissSnackbar(
            message,
            onDismissUpdated,
        )
    }
}

@Composable
fun RetrySnackbar(
    message: String,
    snackbarHostState: SnackbarHostState = LocalSnackbarState.current,
    onDismiss: () -> Unit,
    onRetry: () -> Unit,
) {
    val onDismissUpdated by rememberUpdatedState(onDismiss)
    val onRetryUpdated by rememberUpdatedState(onRetry)
    val retryText = stringResource(id = R.string.retry)
    LaunchedEffect(snackbarHostState, message) {
        snackbarHostState.showRetrySnackbar(
            message = message,
            retryText = retryText,
            onRetry = onRetryUpdated,
            onDismiss = onDismissUpdated,
        )
    }
}

/**
 * Show a snackbar containing the default dismiss action only
 */
suspend fun SnackbarHostState.showDismissSnackbar(
    message: String,
    onDismiss: () -> Unit,
) {
    showSnackbar(
        message,
        withDismissAction = true,
    )
    onDismiss()
}

/**
 * Show a snackbar with a retry action
 */
suspend fun SnackbarHostState.showRetrySnackbar(
    message: String,
    retryText: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
) {
    val result =
        showSnackbar(
            message = message,
            actionLabel = retryText,
            withDismissAction = true,
        )
    when (result) {
        SnackbarResult.Dismissed -> onDismiss()
        SnackbarResult.ActionPerformed -> onRetry()
    }
}
