package br.com.mob1st.core.design.templates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import br.com.mob1st.core.design.atoms.spacing.Spacings
import br.com.mob1st.core.design.atoms.theme.TwoCentsTheme
import br.com.mob1st.core.design.organisms.header.SecondaryTitle
import br.com.mob1st.core.design.organisms.header.TitleDefaults
import br.com.mob1st.core.design.utils.ThemedPreview

/**
 * Template for steps that will be executed inside a feature.
 * It's usually presented after the [FeatureSummaryScaffold].
 * @param snackbarHostState Displays the snackbar in this Scaffold.
 * @param isButtonExpanded Whether the button should be expanded or not.
 * @param onClickBack The action to be performed when the back button is clicked.
 * @param titleContent The title of the screen.
 * @param subtitleContent The subtitle of the screen.
 * @param buttonContent The content to be presented in the button.
 * @param content The content of the screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureStepScaffold(
    snackbarHostState: SnackbarHostState,
    isButtonExpanded: Boolean,
    onClickBack: () -> Unit,
    titleContent: @Composable () -> Unit,
    subtitleContent: @Composable () -> Unit,
    buttonContent: @Composable () -> Unit,
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
            buttonContent()
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
            snackbarHostState = remember { SnackbarHostState() },
            isButtonExpanded = false,
            titleContent = {
                Text(text = "Step title")
            },
            subtitleContent = {
                Text(text = "In this area a short description of the step can be placed.")
            },
            buttonContent = {
                Text(text = "Next")
            },
            content = {
                Text(text = "Content")
            },
        )
    }
}
