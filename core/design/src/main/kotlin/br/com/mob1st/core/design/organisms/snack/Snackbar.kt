package br.com.mob1st.core.design.organisms.snack

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import br.com.mob1st.core.design.atoms.properties.rememberAnnotatedString
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun Snackbar(
    snackbarHostState: SnackbarHostState,
    snackState: SnackState?,
    onDismiss: () -> Unit,
    onPerformAction: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val supporting = snackState?.supporting?.let {
        rememberAnnotatedString(text = snackState.supporting)
    }
    val action = snackState?.action?.let {
        rememberAnnotatedString(text = it)
    }
    val currentOnDismiss by rememberUpdatedState(newValue = onDismiss)
    val currentOnPerformAction by rememberUpdatedState(newValue = onPerformAction)
    LaunchedEffect(key1 = snackbarHostState, key2 = snackState) {
        Timber.d("ptest Showing snackbar: $snackState")
        snackState?.let {
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = checkNotNull(supporting).text,
                    actionLabel = action?.text,
                    withDismissAction = false
                )
                when (result) {
                    SnackbarResult.Dismissed -> currentOnDismiss()
                    SnackbarResult.ActionPerformed -> currentOnPerformAction()
                }
            }
        }
    }
}
