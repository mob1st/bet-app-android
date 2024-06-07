package br.com.mob1st.features.twocents.builder.impl.ui.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.mob1st.core.design.atoms.theme.BetTheme
import br.com.mob1st.core.design.molecules.buttons.PrimaryButton
import br.com.mob1st.core.design.organisms.lists.BulletListItem
import br.com.mob1st.core.design.templates.FeatureSummaryScaffold
import br.com.mob1st.core.design.utils.ThemedPreview
import br.com.mob1st.features.twocents.builder.impl.R

@Composable
internal fun BuilderSummaryPage(
    onClickStart: () -> Unit,
    onBackClicked: () -> Unit,
) {
    FeatureSummaryScaffold(
        onBackClicked = onBackClicked,
        titleContent = {
            Text(stringResource(R.string.builder_summary_header))
        },
        buttonContent = {
            PrimaryButton(onClick = onClickStart) {
                Text(stringResource(R.string.builder_summary_button))
            }
        },
    ) {
        Text(stringResource(R.string.builder_summary_subheader))
        SummaryItems()
    }
}

@Composable
private fun SummaryItems() {
    Column(
        modifier = Modifier
            .padding(top = 64.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BulletListItem(
            headlineContent = {
                Text(stringResource(R.string.builder_summary_item1_headline))
            },
            supportingContent = {
                Text(stringResource(R.string.builder_summary_item1_description))
            },
        )
        BulletListItem(
            headlineContent = {
                Text(stringResource(R.string.builder_summary_item2_headline))
            },
            supportingContent = {
                Text(stringResource(R.string.builder_summary_item2_description))
            },
        )
        BulletListItem(
            headlineContent = {
                Text(stringResource(R.string.builder_summary_item3_headline))
            },
            supportingContent = {
                Text(stringResource(R.string.builder_summary_item3_description))
            },
        )
    }
}

@Composable
@ThemedPreview
private fun BuilderSummaryPagePreview() {
    BetTheme {
        BuilderSummaryPage(
            onClickStart = {},
            onBackClicked = {},
        )
    }
}
