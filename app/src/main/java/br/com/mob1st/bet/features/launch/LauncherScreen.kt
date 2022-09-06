package br.com.mob1st.bet.features.launch

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.bet.core.ui.ds.organisms.FetchedCrossfade
import br.com.mob1st.bet.core.ui.ds.page.DefaultErrorPage
import br.com.mob1st.bet.core.ui.state.AsyncState
import br.com.mob1st.bet.core.ui.state.SimpleMessage
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun LauncherScreen(onFinish: () -> Unit) {
    val viewModel = koinViewModel<LauncherViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LauncherPage(
        state = state,
        onFinish = onFinish,
        onTryAgain = { viewModel.fromUi(LauncherUiEvent.TryAgain(it)) },
    )
}

@Composable
fun LauncherPage(
    state: AsyncState<LaunchData>,
    onFinish: () -> Unit,
    onTryAgain: (message: SimpleMessage) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        FetchedCrossfade(
            state = state,
            emptyError = { _, message ->  DefaultErrorPage(message = message, onTryAgain = onTryAgain) },
            emptyLoading = { CircularProgressIndicator() },
            empty = { DefaultErrorPage(message = state.messages.first(), onTryAgain = onTryAgain) },
        ) {
            LauncherCompleted(onFinish = onFinish)
        }
    }
}

@Composable
fun LauncherCompleted(
    onFinish: () -> Unit,
) {
    Text("Hello")
    val currentOnFinish by rememberUpdatedState(onFinish)
    LaunchedEffect(Unit) {
        delay(1_000)
        currentOnFinish()
    }
}