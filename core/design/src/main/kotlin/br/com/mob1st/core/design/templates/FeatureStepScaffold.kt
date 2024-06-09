package br.com.mob1st.core.design.templates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.mob1st.core.design.R
import br.com.mob1st.core.design.atoms.icons.CheckIcon
import br.com.mob1st.core.design.atoms.theme.BetTheme
import br.com.mob1st.core.design.organisms.header.SecondaryTitle
import br.com.mob1st.core.design.organisms.header.TitleDefaults
import br.com.mob1st.core.design.utils.ThemedPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureStepScaffold(
    onBackClicked: () -> Unit,
    onButtonClicked: () -> Unit,
    isButtonExpanded: Boolean,
    titleContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val scrollBehavior = TitleDefaults.scrollBehavior
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SecondaryTitle(
                scrollBehavior = scrollBehavior,
                titleContent = titleContent,
                onBackClicked = onBackClicked,
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                expanded = isButtonExpanded,
                text = { Text(stringResource(R.string.core_design_next_button_text)) },
                icon = {
                    CheckIcon(
                        contentDescription = stringResource(R.string.core_design_back_button_content_description),
                    )
                },
                onClick = onButtonClicked,
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
            ) {
                content()
            }
        },
    )
}

@Composable
@ThemedPreview
private fun FeatureStepScaffoldPreview() {
    BetTheme {
        FeatureStepScaffold(
            onBackClicked = {},
            onButtonClicked = {},
            isButtonExpanded = false,
            titleContent = {
                Text(text = "Title")
            },
            content = {
                Text(text = "Content")
            },
        )
    }
}
