package br.com.mob1st.features.dev.impl.presentation.menu

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
import br.com.mob1st.core.design.organisms.lists.ListItem
import br.com.mob1st.core.design.organisms.snack.Snackbar
import br.com.mob1st.core.navigation.SideEffectNavigation
import br.com.mob1st.features.dev.publicapi.presentation.DevSettingsNavTarget
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun DevMenuPage(next: (DevSettingsNavTarget) -> Unit) {
    val vm = koinViewModel<DevMenuViewModel>()
    val state by vm.output.collectAsStateWithLifecycle()
    DevMenuPageView(
        pageState = state,
        onSelectItem = vm::selectItem,
        onDismissSnackbar = vm::dismissSnack,
        onNavigate = next,
        onConsumeNavigation = vm::consumeNavigation
    )
}

@Composable
private fun DevMenuPageView(
    pageState: DevMenuPageState,
    onSelectItem: (Int) -> Unit,
    onDismissSnackbar: () -> Unit,
    onNavigate: (DevSettingsNavTarget) -> Unit,
    onConsumeNavigation: () -> Unit,
) {
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
                    modifier = Modifier.clickable { onSelectItem(index) }
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
    SideEffectNavigation(
        target = state.navTarget,
        onNavigate = onNavigate,
        onConsumeNavigation = onConsumeNavigation
    )
}
