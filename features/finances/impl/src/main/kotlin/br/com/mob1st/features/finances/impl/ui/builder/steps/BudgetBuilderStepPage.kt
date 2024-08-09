package br.com.mob1st.features.finances.impl.ui.builder.steps

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.events.builderStepScreenView
import br.com.mob1st.features.finances.impl.ui.categories.components.dialog.CategoryNameDialog
import br.com.mob1st.features.finances.impl.ui.categories.components.item.CategorySectionItem
import br.com.mob1st.features.finances.impl.ui.categories.components.sheet.CategoryBottomSheet
import br.com.mob1st.features.utils.observability.TrackEventSideEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun BudgetBuilderStepPage(
    step: BudgetBuilderAction.Step,
    onNext: (BuilderStepConsumables.NavEvent) -> Unit,
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
        onDismissDialog = { viewModel.consume(BuilderStepConsumables.nullableDialog) },
        onDismissBottomSheet = { viewModel.consume(BuilderStepConsumables.nullableSheet) },
        onBack = onBack,
    )

    BuilderStepsSideEffects(
        step = step,
        consumables = consumables,
        snackbarHostState = snackbarHostState,
        onDismissSnackbar = { viewModel.consume(BuilderStepConsumables.nullableSnackbar) },
        onNavigate = {
            onNext(it)
            viewModel.consume(BuilderStepConsumables.nullableNavEvent)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryBuilderStepScreen(
    snackbarHostState: SnackbarHostState,
    uiState: BudgetBuilderStepUiState.Loaded,
    consumables: BuilderStepConsumables,
    onSelectManualCategory: (position: Int) -> Unit,
    onSelectSuggestion: (position: Int) -> Unit,
    onClickNext: () -> Unit,
    onDismissDialog: () -> Unit,
    onTypeCategoryName: (name: String) -> Unit,
    onSubmitCategoryName: () -> Unit,
    onDismissBottomSheet: () -> Unit,
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
        Dialog(
            dialog = consumables.dialog,
            onDismiss = onDismissDialog,
            onType = onTypeCategoryName,
            onClickSubmit = onSubmitCategoryName,
        )
        BottomSheet(
            sheet = consumables.sheet,
            snackbarHostState = snackbarHostState,
            onDismiss = onDismissBottomSheet,
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
private fun BuilderStepsSideEffects(
    step: BudgetBuilderAction.Step,
    consumables: BuilderStepConsumables,
    snackbarHostState: SnackbarHostState,
    onDismissSnackbar: () -> Unit,
    onNavigate: (BuilderStepConsumables.NavEvent) -> Unit,
) {
    TrackEventSideEffect(event = AnalyticsEvent.builderStepScreenView(step))
    SnackbarSideEffect(
        snackbarHostState = snackbarHostState,
        snackbarVisuals = consumables.snackbar?.resolve(),
        onDismiss = onDismissSnackbar,
        onPerformAction = {},
    )
    NavigationSideEffect(
        target = consumables.navEvent,
        onNavigate = onNavigate,
    )
}

@Composable
private fun Dialog(
    dialog: BuilderStepConsumables.Dialog?,
    onDismiss: () -> Unit,
    onType: (String) -> Unit,
    onClickSubmit: () -> Unit,
) {
    when (dialog) {
        is BuilderStepNameDialog -> CategoryNameDialog(
            state = dialog.state,
            onType = onType,
            onClickSubmit = onClickSubmit,
            onDismiss = onDismiss,
        )

        null -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet(
    sheet: BuilderStepConsumables.Sheet?,
    snackbarHostState: SnackbarHostState,
    onDismiss: () -> Unit,
) {
    when (sheet) {
        is BuilderStepCategorySheet -> CategoryBottomSheet(
            snackbarHostState = snackbarHostState,
            sheetState = rememberModalBottomSheetState(),
            intent = sheet.intent,
            onDismiss = onDismiss,
        )

        null -> {}
    }
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
            onDismissDialog = {},
            onDismissBottomSheet = {},
            onBack = {},
        )
    }
}
