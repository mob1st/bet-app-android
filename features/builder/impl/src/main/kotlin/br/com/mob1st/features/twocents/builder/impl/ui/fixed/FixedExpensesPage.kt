package br.com.mob1st.features.twocents.builder.impl.ui.fixed

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ListItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.core.design.atoms.properties.texts.rememberAnnotatedString
import br.com.mob1st.core.design.organisms.section.section
import br.com.mob1st.core.design.templates.FeatureStepScaffold
import br.com.mob1st.features.twocents.builder.impl.R
import br.com.mob1st.features.twocents.builder.impl.ui.builder.BuilderUiState
import br.com.mob1st.features.twocents.builder.impl.ui.builder.BuilderViewModel
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
    val navigationTarget by viewModel.navigationOutput.collectAsStateWithLifecycle()
    FixedExpensesContent(
        state = uiState,
        onClickBack = onClickBack,
        onClickNext = viewModel::save,
        onSelectManualItem = viewModel::selectManualCategory,
        onSelectSuggestedItem = viewModel::selectSuggestedCategory,
    )
}

@Composable
private fun FixedExpensesContent(
    state: BuilderUiState,
    onClickBack: () -> Unit,
    onClickNext: () -> Unit,
    onSelectManualItem: (Int) -> Unit,
    onSelectSuggestedItem: (Int) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    FeatureStepScaffold(
        isButtonExpanded = true,
        snackbarHostState = snackbarHostState,
        onClickBack = onClickBack,
        onClickNext = onClickNext,
        titleContent = { Text(stringResource(id = R.string.builder_fixed_expenses_header)) },
        subtitleContent = { Text(stringResource(id = R.string.builder_fixed_expenses_subheader)) },
    ) {
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
            headlineContent = { Text(text = rememberAnnotatedString(text = item.name)) },
            trailingContent = { Text(text = item.amount) },
        )
    }
}
