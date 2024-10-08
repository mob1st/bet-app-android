package br.com.mob1st.features.finances.impl.ui.builder.steps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import br.com.mob1st.features.finances.impl.domain.events.builderStepScreenView
import br.com.mob1st.features.finances.impl.ui.category.components.dialog.CategoryNameDialog
import br.com.mob1st.features.finances.impl.ui.category.components.item.CategorySectionItem
import br.com.mob1st.features.utils.observability.TrackEventSideEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun BudgetBuilderStepPage(
    args: BuilderStepNavArgs,
    onNext: (BuilderStepConsumables.NavEvent) -> Unit,
    onBack: () -> Unit,
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val viewModel = koinViewModel<BudgetBuilderStepViewModel> {
        parametersOf(args)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val consumables by viewModel.consumablesState.collectAsStateWithLifecycle()
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
        onBack = onBack,
    )

    BuilderStepsSideEffects(
        args = args,
        consumables = consumables,
        snackbarHostState = snackbarHostState,
        onDismissSnackbar = { viewModel.consume(BuilderStepConsumables.nullableSnackbar) },
        onNavigate = {
            onNext(it)
            viewModel.consume(BuilderStepConsumables.nullableNavEvent)
        },
    )
}

@Composable
private fun CategoryBuilderStepScreen(
    snackbarHostState: SnackbarHostState,
    uiState: BudgetBuilderStepUiState,
    consumables: BuilderStepConsumables,
    onSelectManualCategory: (position: Int) -> Unit,
    onSelectSuggestion: (position: Int) -> Unit,
    onClickNext: () -> Unit,
    onDismissDialog: () -> Unit,
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
            body = uiState.body,
            onSelectManualCategory = onSelectManualCategory,
            onSelectSuggestion = onSelectSuggestion,
        )
        Dialog(
            dialog = consumables.dialog,
            onDismiss = onDismissDialog,
            onType = onTypeCategoryName,
            onClickSubmit = onSubmitCategoryName,
        )
    }
}

@Composable
private fun BudgetBuilderScreenContent(
    body: BudgetBuilderStepUiState.Body,
    onSelectManualCategory: (position: Int) -> Unit,
    onSelectSuggestion: (position: Int) -> Unit,
) {
    when (body) {
        is BuilderStepLoadedBody -> LoadedBody(
            body = body,
            onSelectManualCategory = onSelectManualCategory,
            onSelectSuggestion = onSelectSuggestion,
        )

        BuilderStepLoadingBody -> LoadingBody()
    }
}

@Composable
private fun LoadingBody() {
    // TODO add placholders
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun LoadedBody(
    body: BuilderStepLoadedBody,
    onSelectManualCategory: (position: Int) -> Unit,
    onSelectSuggestion: (position: Int) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = Spacings.x16 * 2),
    ) {
        section(
            items = body.manuallyAdded,
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
            items = body.suggestions,
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
    args: BuilderStepNavArgs,
    consumables: BuilderStepConsumables,
    snackbarHostState: SnackbarHostState,
    onDismissSnackbar: () -> Unit,
    onNavigate: (BuilderStepConsumables.NavEvent) -> Unit,
) {
    TrackEventSideEffect(event = AnalyticsEvent.builderStepScreenView(args))
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
            onBack = {},
        )
    }
}
