package br.com.mob1st.features.finances.impl.ui.category.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.core.androidx.compose.SnackbarSideEffect
import br.com.mob1st.core.design.molecules.keyboard.FunctionKey
import br.com.mob1st.core.design.molecules.keyboard.Key
import br.com.mob1st.core.design.molecules.keyboard.Keyboard
import br.com.mob1st.core.design.molecules.keyboard.NumericKey
import br.com.mob1st.core.design.utils.PreviewTheme
import br.com.mob1st.core.design.utils.ThemedPreview
import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.domain.events.categoryScreenViewEvent
import br.com.mob1st.features.utils.observability.TrackEventSideEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CategoryDetailPage(
    intent: GetCategoryIntent,
    onSubmit: () -> Unit,
) {
    val viewModel = koinViewModel<CategoryViewModel>(
        parameters = { parametersOf(intent) },
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val consumables by viewModel.consumableUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    CategoryDetailPageScaffold(
        uiState = uiState,
        dialog = consumables.dialog,
        snackbarHostState = snackbarHostState,
        onClickKey = viewModel::onClickKey,
        onDismissDialog = {
            viewModel.consume(CategoryDetailConsumables.nullableDialog)
        },
    )
    CategoryPageSideEffects(
        snackbarHostState = snackbarHostState,
        intent = intent,
        isDone = uiState.isDone,
        consumables = consumables,
        onDismissSnackbar = {
            viewModel.consume(CategoryDetailConsumables.nullableCommonErrorSnackbarState)
        },
        onSubmit = onSubmit,
    )
}

@Composable
private fun CategoryDetailPageScaffold(
    uiState: CategoryDetailState,
    dialog: CategoryDetailConsumables.Dialog?,
    snackbarHostState: SnackbarHostState,
    onClickKey: (Key) -> Unit,
    onDismissDialog: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        CategoryDetailPageContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            dialog = dialog,
            onClickKey = onClickKey,
            onDismissDialog = onDismissDialog,
        )
    }
}

@Composable
private fun CategoryDetailPageContent(
    modifier: Modifier,
    uiState: CategoryDetailState,
    dialog: CategoryDetailConsumables.Dialog?,
    onClickKey: (Key) -> Unit,
    onDismissDialog: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Header(
                modifier = modifier.weight(1f),
                uiState = uiState,
            )
            Keyboard(
                modifier = Modifier.wrapContentSize(),
                onClickKey = onClickKey,
            )
        }
        CategoryDialog(dialog = dialog, onDismissDialog)
    }
}

@Composable
private fun Header(
    modifier: Modifier,
    uiState: CategoryDetailState,
) {
    Box(modifier = modifier) {
        when (uiState) {
            is CategoryDetailState.Loaded -> {
                LoadedHeader(uiState = uiState)
            }

            CategoryDetailState.Loading -> {}
        }
    }
}

@Composable
private fun LoadedHeader(
    uiState: CategoryDetailState.Loaded,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = uiState.amount.resolve(),
            style = MaterialTheme.typography.displaySmall,
        )
    }
}

@Composable
private fun CategoryDialog(
    dialog: CategoryDetailConsumables.Dialog?,
    onDismissDialog: () -> Unit,
) {
    when (dialog) {
        is CategoryCalendarDialog -> {
            AlertDialog(
                title = { Text(text = "Sample") },
                text = { Text(text = "This is an example") },
                onDismissRequest = onDismissDialog,
                confirmButton = { /*TODO*/ },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                ),
            )
        }

        is CategoryNameDialog -> {}
        null -> {}
    }
}

@Composable
private fun CategoryPageSideEffects(
    snackbarHostState: SnackbarHostState,
    intent: GetCategoryIntent,
    isDone: Boolean,
    consumables: CategoryDetailConsumables,
    onDismissSnackbar: () -> Unit,
    onSubmit: () -> Unit,
) {
    LaunchedEffect(isDone) {
        if (isDone) {
            onSubmit()
        }
    }
    SnackbarSideEffect(
        snackbarHostState = snackbarHostState,
        snackbarVisuals = consumables.commonErrorSnackbarState?.resolve(),
        onDismiss = onDismissSnackbar,
        onPerformAction = {},
    )
    TrackEventSideEffect(event = AnalyticsEvent.categoryScreenViewEvent(intent))
}

private fun CategoryViewModel.onClickKey(key: Key) {
    when (key) {
        is NumericKey -> type(key.number)
        FunctionKey.Undo -> undo()
        FunctionKey.Calendar -> openCalendar()
        FunctionKey.Decimal -> decimal()
        FunctionKey.Delete -> deleteNumber()
        FunctionKey.Done -> submit()
    }
}

@Composable
@ThemedPreview
private fun CategoryDetailScaffoldPreview() {
    PreviewTheme {
        CategoryDetailPageScaffold(
            uiState = CategoryDetailState.Loaded(
                category = CategoryFixtures.category,
            ),
            dialog = null,
            snackbarHostState = SnackbarHostState(),
            onClickKey = {},
            onDismissDialog = {},
        )
    }
}