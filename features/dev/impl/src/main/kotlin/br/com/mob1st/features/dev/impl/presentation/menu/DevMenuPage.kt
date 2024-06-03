package br.com.mob1st.features.dev.impl.presentation.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.core.design.atoms.theme.BetTheme
import br.com.mob1st.core.design.organisms.snack.Snackbar
import br.com.mob1st.core.design.organisms.snack.snackbar
import br.com.mob1st.features.dev.impl.R
import br.com.mob1st.features.dev.publicapi.presentation.DevSettingsNavTarget
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.features.utils.errors.CommonErrorDialog
import br.com.mob1st.features.utils.navigation.SideEffectNavigation
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun DevMenuPage(
    onNext: (DevSettingsNavTarget) -> Unit,
    onBack: () -> Unit,
) {
    val vm = koinViewModel<DevMenuViewModel>()
    val state by vm.uiOutput.collectAsStateWithLifecycle()
    val snackState by vm.snackbarOutput.collectAsStateWithLifecycle()
    val navTarget by vm.navigationOutput.collectAsStateWithLifecycle()
    val modal by vm.dialogOutput.collectAsStateWithLifecycle()

    DevMenuPageView(
        pageState = state,
        snackbarState = snackState,
        navTarget = navTarget,
        modal = modal,
        onSelectItem = vm::selectItem,
        onDismissSnackbar = vm::consumeSnackbar,
        onPerformSnackbarAction = vm::performSnackbarAction,
        onConsumeNavigation = vm::consumeNavigation,
        onNavigate = onNext,
        onDismissDialog = onBack,
        onClickTryLater = onBack,
        onBack = onBack,
    )
}

@Composable
private fun DevMenuPageView(
    pageState: DevMenuUiState,
    snackbarState: DevMenuSnackbar?,
    navTarget: DevSettingsNavTarget?,
    modal: CommonError?,
    onSelectItem: (Int) -> Unit,
    onDismissSnackbar: () -> Unit,
    onPerformSnackbarAction: () -> Unit,
    onNavigate: (DevSettingsNavTarget) -> Unit,
    onConsumeNavigation: () -> Unit,
    onDismissDialog: () -> Unit,
    onClickTryLater: () -> Unit,
    onBack: () -> Unit,
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val state = pageState as? DevMenuUiState.Loaded
    Scaffold(
        topBar = { TopBar() },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            DevMenuList(
                items = state?.items.orEmpty().toImmutableList(),
                onSelectItem = onSelectItem,
            )
        }
    }

    CommonErrorDialog(
        onDismiss = onDismissDialog,
        onClickTryLater = onClickTryLater,
        commonError = modal,
    )

    Snackbar(
        snackbarHostState = snackbarHostState,
        snackbarVisuals = snackbarState?.let { snackbar(snackbarState) },
        onDismiss = onDismissSnackbar,
        onPerformAction = {},
    )

    SideEffectNavigation(
        target = navTarget,
        onNavigate = onNavigate,
        onConsumeNavigation = onConsumeNavigation,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.dev_menu_title))
        },
    )
}

@Composable
private fun DevMenuList(
    items: ImmutableList<DevMenuUiState.ListItem>,
    onSelectItem: (Int) -> Unit,
) {
    LazyColumn {
        itemsIndexed(items) { index, item ->
            ListItem(
                modifier = Modifier.clickable { onSelectItem(index) },
                headlineContent = { Text(stringResource(id = item.headline)) },
                supportingContent = { Text(stringResource(id = item.supporting)) },
                trailingContent = item.trailing?.let { resId ->
                    { Text(stringResource(resId)) }
                },
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun snackbar(devMenuSnackbar: DevMenuSnackbar): SnackbarVisuals {
    val message = stringResource(
        R.string.dev_menu_snack_todo_supporting,
    )
    return remember(devMenuSnackbar) {
        snackbar(
            message = message,
        )
    }
}

@Composable
@Preview
private fun DevMenuPagePreview() {
    val uiState = DevMenuUiState.Loaded(
        items = listOf(
            DevMenuUiState.ListItem(
                headline = R.string.dev_menu_list_item_gallery_headline,
                supporting = R.string.dev_menu_list_item_gallery_supporting,
            ),
            DevMenuUiState.ListItem(
                headline = R.string.dev_menu_list_item_entrypoints_headline,
                supporting = R.string.dev_menu_list_item_entrypoints_supporting,
            ),
            DevMenuUiState.ListItem(
                headline = R.string.dev_menu_list_item_environment_headline,
                supporting = R.string.dev_menu_list_item_environment_supporting,
                trailing = R.string.dev_menu_list_item_environment_trailing_qa,
            ),
        ).toImmutableList(),
    )
    BetTheme {
        DevMenuPageView(
            pageState = uiState,
            snackbarState = null,
            navTarget = null,
            modal = null,
            onSelectItem = {},
            onDismissSnackbar = {},
            onPerformSnackbarAction = {},
            onNavigate = {},
            onConsumeNavigation = {},
            onDismissDialog = {},
            onClickTryLater = {},
            onBack = {},
        )
    }
}
