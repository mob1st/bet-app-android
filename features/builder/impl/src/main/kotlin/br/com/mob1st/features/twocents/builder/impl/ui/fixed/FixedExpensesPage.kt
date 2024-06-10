package br.com.mob1st.features.twocents.builder.impl.ui.fixed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.mob1st.core.design.atoms.properties.texts.rememberAnnotatedString
import br.com.mob1st.core.design.organisms.section.section
import br.com.mob1st.core.design.templates.FeatureStepScaffold
import br.com.mob1st.features.twocents.builder.impl.R
import br.com.mob1st.features.twocents.builder.impl.ui.builder.BuilderUiState

@Composable
fun FixedExpensesPage(
    onNext: () -> Unit,
    onClickBack: () -> Unit,
) {
}

@Composable
private fun FixedExpensesContent(
    state: BuilderUiState,
    onClickBack: () -> Unit,
    onClickNext: () -> Unit,
    onSelectItem: (Int) -> Unit,
) {
    FeatureStepScaffold(
        onClickBack = onClickBack,
        onClickNext = onClickNext,
        isButtonExpanded = true,
        titleContent = { Text(stringResource(id = R.string.builder_fixed_expenses_header)) },
        subtitleContent = { Text(stringResource(id = R.string.builder_fixed_expenses_subheader)) },
    ) {
        LazyColumn {
            section(
                titleContent = { Text(stringResource(id = R.string.builder_commons_custom_section_headline)) },
                items = state.manuallyAdded,
            ) { index, item ->
                ListItem(
                    modifier = Modifier.clickable { onSelectItem(index) },
                    headlineContent = { Text(text = rememberAnnotatedString(text = item.name)) },
                    trailingContent = { Text(text = item.amount) },
                )
            }
        }
    }
}
