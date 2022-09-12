package br.com.mob1st.bet.core.ui.ds.molecule

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.res.stringResource
import br.com.mob1st.bet.R

@Composable
fun DismissSnackbar(
    snackbarHostState: SnackbarHostState,
    message: String,
    onDismiss: () -> Unit
) {
    val onDismissUpdated by rememberUpdatedState(onDismiss)
    LaunchedEffect(snackbarHostState, message) {
        snackbarHostState.showDismissSnackbar(
            message,
            onDismissUpdated
        )
    }
}

@Composable
fun RetrySnackbar(
    snackbarHostState: SnackbarHostState,
    message: String,
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
            onDismiss = onDismissUpdated
        )
    }
}

/**
 * Show a snackbar containing the default dismiss action only
 */
suspend fun SnackbarHostState.showDismissSnackbar(
    message: String,
    onDismiss: () -> Unit
) {
    showSnackbar(
        message,
        withDismissAction = true
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
    val result = showSnackbar(
        message = message,
        actionLabel = retryText,
        withDismissAction = true
    )
    when (result) {
        SnackbarResult.Dismissed -> onDismiss()
        SnackbarResult.ActionPerformed -> onRetry()
    }
}

/**
 * Provides the current snackbar host state of the composable tree
 */
val LocalSnackbarState = compositionLocalOf {
    SnackbarHostState()
}
