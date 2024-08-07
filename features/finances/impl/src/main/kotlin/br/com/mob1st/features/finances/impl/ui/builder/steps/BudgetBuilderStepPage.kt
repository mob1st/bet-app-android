package br.com.mob1st.features.finances.impl.ui.builder.steps

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.core.androidx.compose.NavigationSideEffect
import br.com.mob1st.core.androidx.compose.SnackbarSideEffect
import br.com.mob1st.core.design.atoms.icons.CheckIcon
import br.com.mob1st.core.design.atoms.spacing.Spacings
import br.com.mob1st.core.design.organisms.buttons.PrimaryButton
import br.com.mob1st.core.design.organisms.section.section
import br.com.mob1st.core.design.templates.FeatureStepScaffold
import br.com.mob1st.core.design.utils.PreviewTheme
import br.com.mob1st.core.design.utils.ThemedPreview
import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.features.finances.impl.R
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.events.builderStepScreenView
import br.com.mob1st.features.finances.impl.ui.utils.components.CategorySectionItem
import br.com.mob1st.features.utils.observability.TrackEventSideEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun BudgetBuilderStepPage(
    step: BuilderNextAction.Step,
    onNext: (BuilderNextAction) -> Unit,
    onBack: () -> Unit,
) {
    Icons.Filled.Check
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val viewModel = koinViewModel<BudgetBuilderStepViewModel> {
        parametersOf(step)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val consumables by viewModel.consumableUiState.collectAsStateWithLifecycle()
    CategoryBuilderStepScreen(
        uiState = uiState,
        consumables = consumables,
        snackbarHostState = snackbarHostState,
        onSelectManualCategory = viewModel::selectManuallyAddedItem,
        onSelectSuggestion = viewModel::selectSuggestedItem,
        onClickNext = viewModel::next,
        onTypeCategoryName = viewModel::typeCategoryName,
        onSubmitCategoryName = viewModel::submitCategoryName,
        onDismissCategoryName = { viewModel.consume(BudgetBuilderStepConsumables.nullableDialog) },
        onBack = onBack,
    )

    BuilderStepsSideEffects(
        step = step,
        consumables = consumables,
        snackbarHostState = snackbarHostState,
        onDismissSnackbar = { viewModel.consume(BudgetBuilderStepConsumables.nullableSnackbar) },
        onNavigate = {
            onNext(it)
            viewModel.consume(BudgetBuilderStepConsumables.nullableAction)
        },
    )
}

@Composable
private fun CategoryBuilderStepScreen(
    snackbarHostState: SnackbarHostState,
    uiState: BudgetBuilderStepUiState.Loaded,
    consumables: BudgetBuilderStepConsumables,
    onSelectManualCategory: (position: Int) -> Unit,
    onSelectSuggestion: (position: Int) -> Unit,
    onClickNext: () -> Unit,
    onDismissCategoryName: () -> Unit,
    onTypeCategoryName: (name: String) -> Unit,
    onSubmitCategoryName: () -> Unit,
    onBack: () -> Unit,
) {
    FeatureStepScaffold(
        snackbarHostState = snackbarHostState,
        isButtonExpanded = true,
        onClickBack = onBack,
        titleContent = {
            Text(text = stringResource(uiState.header.title))
        },
        subtitleContent = {
            Text(text = stringResource(uiState.header.description))
        },
        buttonContent = {
            PrimaryButton(
                isLoading = uiState.isLoadingNext,
                onClick = onClickNext,
                icon = { CheckIcon() },
                text = { Text(stringResource(id = R.string.finances_commons_button_next)) },
            )
        },
    ) {
        BudgetBuilderScreenContent(
            uiState = uiState,
            onSelectManualCategory = onSelectManualCategory,
            onSelectSuggestion = onSelectSuggestion,
        )
        BudgetBuilderDialog(
            dialog = consumables.dialog,
            onType = onTypeCategoryName,
            onDismiss = onDismissCategoryName,
            onSubmit = onSubmitCategoryName,
        )
    }
}

@Composable
private fun BudgetBuilderScreenContent(
    uiState: BudgetBuilderStepUiState.Loaded,
    onSelectManualCategory: (position: Int) -> Unit,
    onSelectSuggestion: (position: Int) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = Spacings.x16 * 2),
    ) {
        section(
            items = uiState.manuallyAdded,
            key = { it.key },
            titleContent = {
                Text(text = stringResource(id = R.string.finances_builder_commons_custom_section_headline))
            },
            itemContent = { index, itemState ->
                CategorySectionItem(
                    state = itemState,
                    onSelect = { onSelectManualCategory(index) },
                )
            },
        )

        section(
            items = uiState.suggestions,
            key = { it.key },
            titleContent = {
                Text(text = stringResource(id = R.string.finances_builder_commons_suggestions_section_headline))
            },
            itemContent = { index, itemState ->
                CategorySectionItem(
                    state = itemState,
                    onSelect = { onSelectSuggestion(index) },
                )
            },
        )
    }
}

@Composable
private fun BudgetBuilderDialog(
    dialog: BudgetBuilderStepDialog?,
    onType: (text: String) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
) {
    if (dialog !is BudgetBuilderStepDialog.EnterName) {
        return
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.finances_builder_commons_dialog_title))
        },
        text = {
            OutlinedTextField(
                value = dialog.name,
                onValueChange = onType,
                label = {
                    Text(text = stringResource(id = R.string.finances_builder_commons_suggestions_dialog_input_hint))
                },
            )
        },
        confirmButton = {
            TextButton(enabled = dialog.isSubmitEnabled, onClick = onSubmit) {
                Text(text = stringResource(id = R.string.finances_builder_commons_dialog_submit_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = android.R.string.cancel))
            }
        },
    )
}

@Composable
private fun BuilderStepsSideEffects(
    step: BuilderNextAction.Step,
    consumables: BudgetBuilderStepConsumables,
    snackbarHostState: SnackbarHostState,
    onDismissSnackbar: () -> Unit,
    onNavigate: (BuilderNextAction) -> Unit,
) {
    TrackEventSideEffect(event = AnalyticsEvent.builderStepScreenView(step))
    SnackbarSideEffect(
        snackbarHostState = snackbarHostState,
        snackbarVisuals = consumables.snackbar?.resolve(),
        onDismiss = onDismissSnackbar,
        onPerformAction = {},
    )
    NavigationSideEffect(
        target = consumables.action,
        onNavigate = onNavigate,
    )
}

@Composable
@ThemedPreview
private fun CategoryBuilderPagePreview() {
    PreviewTheme {
        CategoryBuilderStepScreen(
            uiState = BudgetBuilderStepPreviewFixture.uiState,
            consumables = BudgetBuilderStepPreviewFixture.consumables,
            snackbarHostState = SnackbarHostState(),
            onSelectManualCategory = {},
            onSelectSuggestion = {},
            onClickNext = {},
            onTypeCategoryName = {},
            onSubmitCategoryName = {},
            onDismissCategoryName = {},
            onBack = {},
        )
    }
}
