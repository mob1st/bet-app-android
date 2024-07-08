package br.com.mob1st.core.design.templates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import br.com.mob1st.core.design.R
import br.com.mob1st.core.design.atoms.icons.CheckIcon
import br.com.mob1st.core.design.atoms.spacing.Spacings
import br.com.mob1st.core.design.atoms.theme.TwoCentsTheme
import br.com.mob1st.core.design.organisms.header.SecondaryTitle
import br.com.mob1st.core.design.organisms.header.TitleDefaults
import br.com.mob1st.core.design.utils.ThemedPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureStepScaffold(
    snackbarHostState: SnackbarHostState,
    isButtonExpanded: Boolean,
    onClickBack: () -> Unit,
    onClickNext: () -> Unit,
    titleContent: @Composable () -> Unit,
    subtitleContent: @Composable () -> Unit,
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
                onBackClicked = onClickBack,
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
                onClick = onClickNext,
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = Spacings.x4),
                ) {
                    ProvideTextStyle(value = MaterialTheme.typography.bodySmall) {
                        subtitleContent()
                    }
                }
                Spacer(modifier = Modifier.height(Spacings.x4))
                content()
            }
        },
    )
}

@Composable
@ThemedPreview
private fun FeatureStepScaffoldPreview() {
    TwoCentsTheme {
        FeatureStepScaffold(
            onClickBack = {},
            onClickNext = {},
            snackbarHostState = remember { SnackbarHostState() },
            isButtonExpanded = false,
            titleContent = {
                Text(text = "Step title")
            },
            subtitleContent = {
                Text(text = "In this area a short description of the step can be placed.")
            },
            content = {
                Text(text = "Content")
            },
        )
    }
}
