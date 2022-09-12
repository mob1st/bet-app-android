package br.com.mob1st.bet.features.launch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.bet.R
import br.com.mob1st.bet.core.ui.compose.LocalActivity
import br.com.mob1st.bet.core.ui.state.AsyncState
import br.com.mob1st.bet.core.ui.state.SimpleMessage
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SplashScreen(
    onFinish: () -> Unit
) {
    val viewModel = koinViewModel<LauncherViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SplashPage(
        state = state,
        onFinish = onFinish,
        onTryAgain = { viewModel.fromUi(LauncherUiEvent.TryAgain(it)) },
    )
}

@Composable
fun SplashPage(
    state: AsyncState<LaunchData>,
    onFinish: () -> Unit,
    onTryAgain: (SimpleMessage) -> Unit
) {
    val onFinishCallback by rememberUpdatedState(onFinish)
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_splash_icon),
            contentDescription = stringResource(id = R.string.content_description_launcher),
        )
        if (state.messages.isNotEmpty()) {
            SplashErrorMessageDialog(message = state.messages[0], onTryAgain = onTryAgain)
        }
    }
    if (state.data.finished) {
        LaunchedEffect(Unit) {
            onFinishCallback()
        }
    }
}

@Composable
fun SplashErrorMessageDialog(
    message: SimpleMessage,
    onTryAgain: (SimpleMessage) -> Unit
) {
    val activity = LocalActivity.current
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.general_message_error_title)) },
        text = { Text(text = stringResource(id = message.descriptionResId)) },
        confirmButton = {
            TextButton(onClick = { onTryAgain(message) }) {
                Text(text = stringResource(id = R.string.try_again))
            }
        },
        dismissButton = {
            TextButton(onClick = { activity.finish() }) {
                Text(text = stringResource(id = android.R.string.cancel))
            }
        },
        onDismissRequest = { activity.finish() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
        )
    )
}