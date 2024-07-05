package br.com.mob1st.features.twocents.builder.impl.ui.fixed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.core.design.molecules.buttons.PrimaryButton
import br.com.mob1st.core.design.organisms.lists.selectableItem
import br.com.mob1st.core.design.organisms.section.section
import br.com.mob1st.core.design.organisms.snack.Snackbar
import br.com.mob1st.core.design.templates.FeatureStepScaffold
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import br.com.mob1st.features.twocents.builder.impl.R
import br.com.mob1st.features.twocents.builder.impl.ui.builder.BuilderListItemState
import br.com.mob1st.features.twocents.builder.impl.ui.builder.BuilderUiState
import br.com.mob1st.features.twocents.builder.impl.ui.builder.BuilderViewModel
import br.com.mob1st.features.twocents.builder.impl.ui.builder.CategoryNameDialogState
import br.com.mob1st.features.twocents.builder.impl.ui.builder.CategorySheetState
import br.com.mob1st.features.twocents.builder.impl.ui.builder.SuggestedCategoryBuilderSection
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.features.utils.errors.toSnackbarVisuals
import br.com.mob1st.features.utils.navigation.SideEffectNavigation
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun FixedExpensesPage(
    onNext: () -> Unit,
    onClickBack: () -> Unit,
) {
    val viewModel = koinViewModel<BuilderViewModel>(
        parameters = {
            parametersOf(CategoryType.Fixed)
        },
    )
    val uiState by viewModel.uiStateOutput.collectAsStateWithLifecycle()
    val error by viewModel.snackbarOutput.collectAsStateWithLifecycle()
    val dialog by viewModel.dialogOutput.collectAsStateWithLifecycle()
    val bottomSheet by viewModel.sheetOutput.collectAsStateWithLifecycle()
    val navTarget by viewModel.navigationOutput.collectAsStateWithLifecycle()

    FixedExpensesContent(
        state = uiState,
        error = null,
        dialog = dialog,
        bottomSheet = bottomSheet,
        onClickBack = onClickBack,
        onClickNext = viewModel::save,
        onSelectManualItem = viewModel::selectManualCategory,
        onSelectSuggestedItem = viewModel::selectSuggestedCategory,
        onDismissSnackbar = viewModel::consumeSnackbar,
        onSubmitName = viewModel::submitCategoryName,
        onDismissDialog = viewModel::consumeDialog,
        onTypeName = viewModel::setCategoryName,
        onSubmitSheet = viewModel::submitCategory,
        onDismissSheet = viewModel::consumeSheet,
        onClickAddItem = viewModel::showCategoryNameDialog,
        onTypeCategoryAmount = viewModel::setCategoryAmount,
    )

    SideEffectNavigation(
        target = navTarget,
        onNavigate = { onNext() },
        onConsumeNavigation = viewModel::consumeNavigation,
    )
}

@Composable
private fun FixedExpensesContent(
    state: BuilderUiState,
    error: CommonError?,
    dialog: CategoryNameDialogState?,
    bottomSheet: CategorySheetState?,
    onClickBack: () -> Unit,
    onClickNext: () -> Unit,
    onSelectManualItem: (Int) -> Unit,
    onClickAddItem: () -> Unit,
    onSelectSuggestedItem: (Int) -> Unit,
    onDismissSnackbar: () -> Unit,
    onSubmitName: () -> Unit,
    onDismissDialog: () -> Unit,
    onSubmitSheet: () -> Unit,
    onDismissSheet: () -> Unit,
    onTypeCategoryAmount: (String) -> Unit,
    onTypeName: (String) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    FeatureStepScaffold(
        isButtonExpanded = true,
        snackbarHostState = snackbarHostState,
        onClickBack = onClickBack,
        onClickNext = onClickNext,
        titleContent = { Text(stringResource(R.string.builder_fixed_expenses_header)) },
        subtitleContent = { Text(stringResource(R.string.builder_fixed_expenses_subheader)) },
    ) {
        Box {
            // manual items + add item button
            val allItems = remember(state.manuallyAddedSection.categories) {
                (state.manuallyAddedSection.categories + Unit).toImmutableList()
            }
            LazyColumn {
                customSection(
                    allItems = allItems,
                    onSelectItem = onSelectManualItem,
                    onClickAdd = onClickAddItem,
                )
                suggestedSection(
                    state.suggestedSection,
                    onSelectItem = onSelectSuggestedItem,
                )
            }
        }

        dialog?.let { categoryNameDialog ->
            CategoryNameDialog(
                state = categoryNameDialog,
                onTypeText = onTypeName,
                onDismissDialog = onDismissDialog,
                onClickConfirm = onSubmitName,
            )
        }

        bottomSheet?.let { categorySheet ->
            CategoryBottomSheet(
                state = categorySheet,
                onSubmitSheet = onSubmitSheet,
                onDismissSheet = onDismissSheet,
                onTypeCategoryAmount = onTypeCategoryAmount,
            )
        }
    }

    Snackbar(
        snackbarHostState = snackbarHostState,
        snackbarVisuals = error?.let { error.toSnackbarVisuals() },
        onDismiss = onDismissSnackbar,
        onPerformAction = {},
    )
}

private fun LazyListScope.customSection(
    allItems: ImmutableList<Any>,
    onSelectItem: (Int) -> Unit,
    onClickAdd: () -> Unit,
) {
    section(
        titleContent = { Text(stringResource(R.string.builder_commons_custom_section_headline)) },
        itemsContentType = { index, _ ->
            if (index == allItems.lastIndex) {
                CustomSectionType.Add
            } else {
                CustomSectionType.Item
            }
        },
        items = allItems,
    ) { index, item ->
        if (item is BuilderListItemState) {
            ListItem(
                modifier = Modifier.selectableItem { onSelectItem(index) },
                headlineContent = { Text(item.name.resolve()) },
                trailingContent = { Text(item.amount) },
            )
        } else {
            ListItem(
                modifier = Modifier.selectableItem { onClickAdd() },
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.builder_commons_custom_section_add_item),
                    )
                },
            )
        }
    }
}

private fun LazyListScope.suggestedSection(
    section: SuggestedCategoryBuilderSection,
    onSelectItem: (Int) -> Unit,
) {
    section(
        titleContent = { Text(stringResource(R.string.builder_commons_suggestions_section_headline)) },
        items = section.categories,
    ) { index, item ->
        ListItem(
            modifier = Modifier.selectableItem { onSelectItem(index) },
            headlineContent = { Text(item.name.resolve()) },
            trailingContent = { Text(item.amount) },
        )
    }
}

@Composable
private fun CategoryNameDialog(
    state: CategoryNameDialogState,
    onTypeText: (String) -> Unit,
    onDismissDialog: () -> Unit,
    onClickConfirm: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    AlertDialog(
        onDismissRequest = onDismissDialog,
        title = { Text(stringResource(R.string.builder_commons_suggestions_dialog_title)) },
        confirmButton = {
            TextButton(onClick = onClickConfirm, enabled = state.isSubmitEnabled) {
                Text(stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissDialog) {
                Text(stringResource(android.R.string.cancel))
            }
        },
        text = {
            OutlinedTextField(
                value = state.text,
                onValueChange = onTypeText,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            keyboardController?.show()
                        }
                    },
            )
        },
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryBottomSheet(
    state: CategorySheetState,
    onSubmitSheet: () -> Unit,
    onDismissSheet: () -> Unit,
    onTypeCategoryAmount: (String) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissSheet,
    ) {
        Column {
            Text(state.name.resolve())
            OutlinedTextField(
                value = state.amount,
                onValueChange = onTypeCategoryAmount,
                modifier = Modifier.fillMaxWidth(),
            )
            PrimaryButton(
                onClick = onSubmitSheet,
            ) {
                Text(text = "Set")
            }
        }
    }
}

private enum class CustomSectionType {
    Add,
    Item,
}
