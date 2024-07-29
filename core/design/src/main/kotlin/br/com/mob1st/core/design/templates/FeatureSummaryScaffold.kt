package br.com.mob1st.core.design.templates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import br.com.mob1st.core.design.molecules.buttons.Button
import br.com.mob1st.core.design.organisms.header.PrimaryTitle
import br.com.mob1st.core.design.organisms.header.TitleDefaults
import br.com.mob1st.core.design.utils.ThemedPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureSummaryScaffold(
    snackbarHostState: SnackbarHostState,
    onClickButton: () -> Unit = {},
    titleContent: @Composable () -> Unit,
    buttonContent: @Composable (() -> Unit),
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
            ExtendedFloatingActionButton(onClick = onClickButton) {
                buttonContent()
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Spacings.x4)
                .padding(bottom = Spacings.x4),
        ) {
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
    TwoCentsTheme {
        FeatureSummaryScaffold(
            snackbarHostState = remember {
                SnackbarHostState()
            },
            titleContent = {
                Text(text = "Some screen title")
            },
            content = {
                Text(text = "Here is the content")
            },
            buttonContent = {
                Button(onClick = { }) {
                    Text(text = "Some button")
                }
            },
        )
    }
}
