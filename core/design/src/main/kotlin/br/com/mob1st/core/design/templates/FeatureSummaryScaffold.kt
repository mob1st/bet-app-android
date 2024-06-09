package br.com.mob1st.core.design.templates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import br.com.mob1st.core.design.atoms.spacing.Spacings
import br.com.mob1st.core.design.atoms.theme.BetTheme
import br.com.mob1st.core.design.molecules.buttons.Button
import br.com.mob1st.core.design.organisms.header.PrimaryTitle
import br.com.mob1st.core.design.organisms.header.TitleDefaults
import br.com.mob1st.core.design.utils.ThemedPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureSummaryScaffold(
    titleContent: @Composable () -> Unit,
    buttonContent: @Composable (() -> Unit),
    onBackClicked: (() -> Unit),
    content: @Composable () -> Unit,
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
                onBackClicked = onBackClicked,
            )
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
            buttonContent()
        }
    }
}

@Composable
@ThemedPreview
private fun FeatureSummaryScaffoldPreview() {
    BetTheme {
        FeatureSummaryScaffold(
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
            onBackClicked = {},
        )
    }
}
