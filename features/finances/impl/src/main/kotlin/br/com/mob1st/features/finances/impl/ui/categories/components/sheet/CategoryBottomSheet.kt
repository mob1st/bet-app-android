@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.mob1st.features.finances.impl.ui.categories.components.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.core.androidx.compose.SnackbarSideEffect
import br.com.mob1st.core.design.molecules.keyboard.Key
import br.com.mob1st.core.design.molecules.keyboard.Keyboard
import br.com.mob1st.core.design.utils.PreviewTheme
import br.com.mob1st.core.design.utils.ThemedPreview
import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.domain.events.categoryScreenViewEvent
import br.com.mob1st.features.utils.observability.TrackEventSideEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CategoryBottomSheet(
    snackbarHostState: SnackbarHostState,
    intent: GetCategoryIntent,
    onDismiss: () -> Unit,
) {
    val viewModel = koinViewModel<CategoryViewModel>(
        parameters = { parametersOf(intent) },
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val consumables by viewModel.consumableUiState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        CategoryBottomSheetContent(
            uiState = uiState,
            onClickKey = {},
        )
    }
    CategoryBottomSheetSideEffects(
        snackbarHostState = snackbarHostState,
        intent = intent,
        isDone = uiState.isDone,
        consumables = consumables,
        onDismissSnackbar = {
            viewModel.consume(CategoryDetailConsumables.nullableCommonErrorSnackbarState)
        },
        onDone = viewModel::submit,
    )
}

@Composable
private fun CategoryBottomSheetContent(
    uiState: CategoryDetailState,
    onClickKey: (Key) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxHeight(0.95f),
    ) {
        Header(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            uiState = uiState,
        )
        Keyboard(onClickKey = onClickKey)
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
private fun CategoryBottomSheetSideEffects(
    snackbarHostState: SnackbarHostState,
    intent: GetCategoryIntent,
    isDone: Boolean,
    consumables: CategoryDetailConsumables,
    onDismissSnackbar: () -> Unit,
    onDone: () -> Unit,
) {
    LaunchedEffect(isDone) {
        if (isDone) {
            onDone()
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

@Composable
@ThemedPreview
private fun CategoryBottomSheetContentPreview() {
    PreviewTheme {
        CategoryBottomSheetContent(
            uiState = CategoryDetailState.Loaded(
                category = CategoryFixtures.category,
            ),
            onClickKey = {},
        )
    }
}
