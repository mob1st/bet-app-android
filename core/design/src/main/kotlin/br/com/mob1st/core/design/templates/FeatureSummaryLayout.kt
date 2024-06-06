package br.com.mob1st.core.design.templates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import br.com.mob1st.core.design.atoms.spacing.Spacings
import br.com.mob1st.core.design.atoms.theme.BetTheme
import br.com.mob1st.core.design.molecules.buttons.Button
import br.com.mob1st.core.design.utils.ThemedPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureSummaryScaffold(
    titleContent: @Composable () -> Unit,
    buttonContent: @Composable (() -> Unit),
    onBackClicked: (() -> Unit),
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            Header(
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
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
                modifier = Modifier.weight(1f).fillMaxWidth(),
            ) {
                content()
            }
            buttonContent()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Header(
    scrollBehavior: TopAppBarScrollBehavior,
    titleContent: @Composable () -> Unit,
    onBackClicked: (() -> Unit),
) {
    LargeTopAppBar(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        navigationIcon = {
            IconButton(
                onClick = onBackClicked,
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
        title = {
            titleContent()
        },
    )
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
