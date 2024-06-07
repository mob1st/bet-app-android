package br.com.mob1st.features.twocents.builder.impl.ui.fixed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FixedExpensesPage(
    onClickNext: () -> Unit,
    onClickBack: () -> Unit,
) {
    Scaffold(
        topBar = {},
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            FixedExpensesContent()
        }
    }
}

@Composable
private fun FixedExpensesContent() {
}
