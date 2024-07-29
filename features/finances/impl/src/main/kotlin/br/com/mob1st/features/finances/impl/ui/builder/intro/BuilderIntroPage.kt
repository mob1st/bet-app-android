package br.com.mob1st.features.finances.impl.ui.builder.intro

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.core.androidx.compose.NavigationSideEffect
import br.com.mob1st.core.androidx.compose.SnackbarSideEffect
import br.com.mob1st.core.design.molecules.loading.Loading
import br.com.mob1st.core.design.templates.FeatureSummaryScaffold
import br.com.mob1st.core.design.utils.ThemedPreview
import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.features.finances.impl.domain.events.builderIntroScreenViewEvent
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRoute
import br.com.mob1st.features.utils.observability.TrackEventSideEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun BuilderIntroPage(
    onNext: (BuilderRoute) -> Unit,
) {
    val viewModel = koinViewModel<BuilderIntroViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val consumables by viewModel.consumableUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    BuilderIntroScreen(
        snackbarHostState = snackbarHostState,
        uiState = uiState,
        onClickNext = viewModel::start,
    )
    IntroSideEffects(
        consumables = consumables,
        snackbarHostState = snackbarHostState,
        onNavigate = {
            onNext(it)
            viewModel.consume(BuilderIntroConsumables.nullableRoute)
        },
        onDismissSnackbar = {
            viewModel.consume(BuilderIntroConsumables.nullableSnackbar)
        },
    )
}

@Composable
private fun BuilderIntroScreen(
    snackbarHostState: SnackbarHostState,
    uiState: BuilderIntroUiState,
    onClickNext: () -> Unit,
) {
    FeatureSummaryScaffold(
        snackbarHostState = snackbarHostState,
        titleContent = { Text(text = "Intro Title") },
        onClickButton = onClickNext,
        buttonContent = { IntroButtonContent(isLoading = uiState.isLoading) },
    ) {
        IntroScaffoldContent()
    }
}

@Composable
private fun BoxScope.IntroScaffoldContent() {
    Text(
        modifier = Modifier.align(Alignment.Center),
        text = "Intro",
    )
}

@Composable
private fun IntroButtonContent(isLoading: Boolean) {
    Loading(
        isLoading = isLoading,
        crossfadeLabel = "loadingButton",
    ) {
        Text(text = "Next")
    }
}

@Composable
private fun IntroSideEffects(
    consumables: BuilderIntroConsumables,
    snackbarHostState: SnackbarHostState,
    onNavigate: (BuilderRoute) -> Unit,
    onDismissSnackbar: () -> Unit,
) {
    TrackEventSideEffect(event = AnalyticsEvent.builderIntroScreenViewEvent())
    NavigationSideEffect(
        target = consumables.route,
        onNavigate = onNavigate,
    )
    SnackbarSideEffect(
        snackbarHostState = snackbarHostState,
        snackbarVisuals = consumables.snackbar?.resolve(),
        onDismiss = onDismissSnackbar,
        onPerformAction = {},
    )
}

@Composable
@ThemedPreview
private fun BuilderIntroScreenPreview() {
    BuilderIntroPage(
        onNext = {},
    )
}
