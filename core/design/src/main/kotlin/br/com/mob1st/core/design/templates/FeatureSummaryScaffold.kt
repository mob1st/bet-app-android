package br.com.mob1st.core.design.templates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import br.com.mob1st.core.design.organisms.header.PrimaryTitle
import br.com.mob1st.core.design.organisms.header.TitleDefaults
import br.com.mob1st.core.design.utils.PreviewTheme
import br.com.mob1st.core.design.utils.ThemedPreview

/**
 * Template for the entry point of a feature, used to provide context about the steps that will be
 * taken in the feature.
 * @param snackbarHostState Displays the snackbar in this Scaffold.
 * @param titleContent The title of the screen.
 * @param subtitleContent The subtitle of the screen.
 * @param buttonContent The content to be presented in the button.
 * @param content The content of the screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureSummaryScaffold(
    snackbarHostState: SnackbarHostState,
    titleContent: @Composable () -> Unit,
    subtitleContent: @Composable () -> Unit,
    buttonContent: @Composable () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val titleScrollBehavior = TitleDefaults.scrollBehavior
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(titleScrollBehavior.nestedScrollConnection),
        topBar = {
            PrimaryTitle(
                scrollBehavior = titleScrollBehavior,
                titleContent = titleContent,
            )
        },
        floatingActionButton = {
            buttonContent()
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = Spacings.x4),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacings.x4),
            ) {
                ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                    subtitleContent()
                }
            }
            Spacer(modifier = Modifier.height(Spacings.x16))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                content()
            }
        }
    }
}

@Composable
@ThemedPreview
private fun FeatureSummaryScaffoldPreview() {
    PreviewTheme {
        FeatureSummaryScaffold(
            snackbarHostState = remember {
                SnackbarHostState()
            },
            titleContent = {
                Text(text = "Some screen title")
            },
            subtitleContent = {
                Text(text = "Some subtitle")
            },
            content = {
                Text(text = "Here is the content")
            },
            buttonContent = {
                Text(text = "Click me")
            },
        )
    }
}
