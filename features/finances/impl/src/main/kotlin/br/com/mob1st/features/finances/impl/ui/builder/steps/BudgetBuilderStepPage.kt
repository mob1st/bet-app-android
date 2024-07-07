package br.com.mob1st.features.finances.impl.ui.builder.steps

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.core.design.atoms.theme.BetTheme
import br.com.mob1st.core.design.organisms.lists.selectableItem
import br.com.mob1st.core.design.organisms.section.section
import br.com.mob1st.core.design.organisms.snack.Snackbar
import br.com.mob1st.core.design.templates.FeatureStepScaffold
import br.com.mob1st.core.design.utils.ThemedPreview
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.R
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.utils.navigation.SideEffectNavigation
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BudgetBuilderStepPage(
    step: BuilderNextAction.Step,
    onNext: (BuilderNextAction) -> Unit,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<BudgetBuilderStepViewModel> {
        parametersOf(step)
    }
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
        onNavigate = {
            when (it) {
                is BudgetBuilderStepNavEvent.AddBudgetCategory -> {}
                is BudgetBuilderStepNavEvent.EditBudgetCategory -> {}
                is BudgetBuilderStepNavEvent.NextAction -> onNext(it.action)
            }
            viewModel.consume(BudgetBuilderStepConsumables.nullableNavEvent)
        },
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
    onNavigate: (BudgetBuilderStepNavEvent) -> Unit,
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
    SideEffectNavigation(
        target = consumables.navEvent,
        onNavigate = onNavigate,
    )
    if (uiState !is BudgetBuilderStepUiState.Packed) {
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
    uiState: BudgetBuilderStepUiState.Packed,
    onSelectManualCategory: (position: Int) -> Unit,
    onSelectSuggestion: (position: Int) -> Unit,
) {
    LazyColumn {
        section(
            items = uiState.manuallyAdded,
            key = { it.key },
            titleContent = {
                Text(text = stringResource(id = R.string.finances_builder_commons_custom_section_headline))
            },
            itemContent = { index, item ->
                CategoryListItemContent(
                    index = index,
                    categoryListItem = item,
                    onSelectItem = onSelectManualCategory,
                )
            },
        )
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
        section(
            items = uiState.suggestions,
            key = { it.key },
            titleContent = {
                Text(text = stringResource(id = R.string.finances_builder_commons_suggestions_section_headline))
            },
            itemContent = { index, item ->
                CategoryListItemContent(
                    index = index,
                    categoryListItem = item,
                    onSelectItem = onSelectSuggestion,
                )
            },
        )
    }
}

@Composable
private fun CategoryListItemContent(
    index: Int,
    categoryListItem: CategoryListItem,
    onSelectItem: (Int) -> Unit,
) {
    val trailingContent: (@Composable () -> Unit)? =
        categoryListItem.value?.let {
            {
                Text(text = it.resolve())
            }
        }
    val supportingContent: (@Composable () -> Unit)? =
        categoryListItem.supporting?.let {
            {
                Text(text = it.resolve())
            }
        }
    ListItem(
        modifier = Modifier.selectableItem { onSelectItem(index) },
        headlineContent = {
            Text(text = categoryListItem.headline.resolve())
        },
        supportingContent = supportingContent,
        trailingContent = trailingContent,
    )
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
@ThemedPreview
private fun CategoryBuilderPagePreview() {
    val uiState = BudgetBuilderStepUiState.Packed(
        builder = BudgetBuilder(
            id = FixedIncomesStep,
            manuallyAdded = listOf(
                Category(
                    id = Category.Id(),
                    name = "Category 1",
                    amount = Money(1000),
                    isExpense = true,
                    recurrences = Recurrences.Fixed(DayOfMonth(1)),
                ),
            ),
            suggestions = listOf(
                CategorySuggestion(
                    id = CategorySuggestion.Id(),
                    nameResId = R.string.finances_builder_suggestions_item_gym,
                    linkedCategory = null,
                ),
            ),
        ),
    )
    val consumables = BudgetBuilderStepConsumables()
    BetTheme {
        CategoryBuilderStepScreen(
            uiState = uiState,
            consumables = consumables,
            onSelectManualCategory = {},
            onSelectSuggestion = {},
            onClickNext = {},
            onDismissSnackbar = {},
            onTypeCategoryName = {},
            onSubmitCategoryName = {},
            onDismissCategoryName = {},
            onNavigate = {},
            onBack = {},
        )
    }
}
