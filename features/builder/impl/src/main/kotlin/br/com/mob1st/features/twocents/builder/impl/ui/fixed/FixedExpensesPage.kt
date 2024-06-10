package br.com.mob1st.features.twocents.builder.impl.ui.fixed

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.core.design.atoms.properties.texts.rememberAnnotatedString
import br.com.mob1st.core.design.molecules.buttons.PrimaryButton
import br.com.mob1st.core.design.organisms.section.section
import br.com.mob1st.core.design.organisms.snack.Snackbar
import br.com.mob1st.core.design.templates.FeatureStepScaffold
import br.com.mob1st.features.twocents.builder.impl.R
import br.com.mob1st.features.twocents.builder.impl.ui.builder.BuilderUiState
import br.com.mob1st.features.twocents.builder.impl.ui.builder.BuilderViewModel
import br.com.mob1st.features.twocents.builder.impl.ui.builder.CategoryNameDialogState
import br.com.mob1st.features.twocents.builder.impl.ui.builder.CategorySheetState
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.features.utils.errors.toSnackbarVisuals
import br.com.mob1st.features.utils.navigation.SideEffectNavigation
import kotlinx.collections.immutable.ImmutableList
import org.koin.androidx.compose.koinViewModel

@Composable
fun FixedExpensesPage(
    onNext: () -> Unit,
    onClickBack: () -> Unit,
) {
    val viewModel = koinViewModel<BuilderViewModel>()
    val uiState by viewModel.uiStateOutput.collectAsStateWithLifecycle()
    val error by viewModel.snackbarOutput.collectAsStateWithLifecycle()
    val dialog by viewModel.dialogOutput.collectAsStateWithLifecycle()
    val bottomSheet by viewModel.sheetOutput.collectAsStateWithLifecycle()
    val navTarget by viewModel.navigationOutput.collectAsStateWithLifecycle()

    FixedExpensesContent(
        state = uiState,
        error = error,
        dialog = dialog,
        bottomSheet = bottomSheet,
        onClickBack = onClickBack,
        onClickNext = viewModel::save,
        onSelectManualItem = viewModel::selectManualCategory,
        onSelectSuggestedItem = viewModel::selectSuggestedCategory,
        onDismissSnackbar = viewModel::consumeSnackbar,
        onSubmitName = viewModel::submitManualCategoryName,
        onDismissDialog = viewModel::consumeDialog,
        onTypeName = viewModel::setCategoryName,
        onSubmitSheet = viewModel::submitCategory,
        onDismissSheet = viewModel::consumeSheet,
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
            LazyColumn {
                builderSection(
                    title = R.string.builder_commons_custom_section_headline,
                    items = state.manuallyAddedSection,
                    onSelectItem = onSelectManualItem,
                )
                builderSection(
                    title = R.string.builder_commons_suggestions_section_headline,
                    items = state.suggestedSection,
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
                onSubmitSheet = { /*TODO*/ },
                onDismissSheet = { /*TODO*/ },
                onTypeCategoryAmount = {},
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

private fun LazyListScope.builderSection(
    @StringRes title: Int,
    items: ImmutableList<BuilderUiState.ListItem>,
    onSelectItem: (Int) -> Unit,
) {
    section(
        titleContent = {
            Text(stringResource(title))
        },
        items = items,
    ) { index, item ->
        ListItem(
            modifier = Modifier.clickable { onSelectItem(index) },
            headlineContent = { Text(rememberAnnotatedString(item.name)) },
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
    AlertDialog(
        onDismissRequest = onDismissDialog,
        title = {
            Text(stringResource(R.string.builder_commons_suggestions_dialog_title))
        },
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
            )
        },
    )
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
            Text(rememberAnnotatedString(state.category))
            OutlinedTextField(
                value = state.amount,
                onValueChange = onTypeCategoryAmount,
            )
            PrimaryButton(
                onClick = onSubmitSheet,
            ) {
                Text(text = "Set")
            }
        }
    }
}
