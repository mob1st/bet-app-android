package br.com.mob1st.features.home.impl.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun HomePage() {
    Scaffold(
        topBar = {},
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { /*TODO*/ },
                    icon = { /*TODO*/ },
                )
            }
        },
        snackbarHost = {
        },
        floatingActionButton = {
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
        ) {
        }
    }
}

private fun HomePageView() {
}

@Preview
@Composable
private fun HomePagePreview() {
    HomePage()
}
