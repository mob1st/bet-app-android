package br.com.mob1st.features.dev.impl.menu.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.mob1st.core.design.organisms.lists.ListItem
import br.com.mob1st.core.design.organisms.snack.Snackbar

@Composable
fun DevMenuPage() {
    val vm = viewModel<DevMenuViewModel>()
    val state by vm.output.collectAsStateWithLifecycle()
    DevMenuPageView(
        pageState = state,
        onSelectItem = vm::selectItem,
        onDismissSnackbar = vm::dismissSnack
    )
}

@Composable
private fun DevMenuPageView(pageState: DevMenuPageState, onSelectItem: (Int) -> Unit, onDismissSnackbar: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state = pageState as? DevMenuPageState.Loaded ?: return
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            itemsIndexed(state.items) { index, item ->
                ListItem(
                    state = item,
                    modifier = Modifier
                        .clickable {
                            onSelectItem(index)
                        }
                )
                Divider(modifier = Modifier.fillMaxWidth())
            }
        }
    }

    Snackbar(
        snackbarHostState = snackbarHostState,
        snackState = state.snack,
        onDismiss = onDismissSnackbar,
        onPerformAction = { }
    )
}
