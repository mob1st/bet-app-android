package br.com.mob1st.features.finances.impl.ui.builder.steps

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.core.design.organisms.snack.Snackbar
import br.com.mob1st.core.design.templates.FeatureStepScaffold
import br.com.mob1st.core.design.utils.ThemedPreview
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import org.koin.androidx.compose.koinViewModel

@Composable
fun BudgetBuilderStepPage(
    step: BuilderNextAction.Step,
    onNext: (BuilderNextAction) -> Unit,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<BudgetBuilderStepViewModel>()
    val uiState by viewModel.uiStateOutput.collectAsStateWithLifecycle()
    val consumables by viewModel.consumableUiState.collectAsStateWithLifecycle()
    CategoryBuilderStepScreen(
        uiState = uiState,
        consumables = consumables,
        onSelectManualCategory = viewModel::selectManuallyAddedItem,
        onSelectSuggestion = viewModel::selectSuggestedItem,
        onClickNext = viewModel::next,
        onDismissSnackbar = { viewModel.consume(BudgetBuilderStepConsumables.nullableSnackbar) },
        onTypeCategoryName = viewModel::typeCategoryName,
        onSubmitCategoryName = viewModel::submitCategoryName,
        onDismissCategoryName = { viewModel.consume(BudgetBuilderStepConsumables.nullableDialog) },
        onNavigate = { viewModel.consume(BudgetBuilderStepConsumables.nullableNavEvent) },
        onBack = onBack,
    )
}

@Composable
private fun CategoryBuilderStepScreen(
    uiState: BudgetBuilderStepUiState,
    consumables: BudgetBuilderStepConsumables,
    onSelectManualCategory: (position: Int) -> Unit,
    onSelectSuggestion: (position: Int) -> Unit,
    onClickNext: () -> Unit,
    onDismissSnackbar: () -> Unit,
    onTypeCategoryName: (name: String) -> Unit,
    onSubmitCategoryName: () -> Unit,
    onDismissCategoryName: () -> Unit,
    onNavigate: () -> Unit,
    onBack: () -> Unit,
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    Snackbar(
        snackbarHostState = snackbarHostState,
        snackbarVisuals = consumables.snackbar?.resolve(),
        onDismiss = onDismissSnackbar,
        onPerformAction = {},
    )
    if (uiState !is FilledBudgetBuilderStepUiState) {
        return
    }
    FeatureStepScaffold(
        snackbarHostState = snackbarHostState,
        isButtonExpanded = true,
        onClickBack = onBack,
        onClickNext = onClickNext,
        titleContent = {
            Text(text = stringResource(id = uiState.header.title))
        },
        subtitleContent = {
            Text(text = stringResource(id = uiState.header.description))
        },
    ) {
    }
}

@Composable
@ThemedPreview
private fun CategoryBuilderPagePreview() {
}
