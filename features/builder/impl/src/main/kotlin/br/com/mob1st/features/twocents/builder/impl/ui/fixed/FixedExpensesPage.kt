package br.com.mob1st.features.twocents.builder.impl.ui.fixed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.mob1st.features.twocents.builder.impl.R

@Composable
fun FixedExpensesPage(
    onClickNext: () -> Unit,
    onClickBack: () -> Unit,
) {
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FixedExpensesContent() {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                navigationIcon = {
                },
                title = {
                    Text(stringResource(id = R.string.builder_fixed_expenses_header))
                },
            )
        },
        floatingActionButton = {
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            FixedExpensesContent()
        }
    }
}
