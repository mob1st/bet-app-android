package br.com.mob1st.features.finances.impl.ui.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.features.utils.errors.CommonError
import org.koin.androidx.compose.koinViewModel

@Composable
fun CashFlowTabPage(onClickClose: () -> Unit) {
    val viewModel = koinViewModel<CashFlowViewModel>()
    val uiState by viewModel.uiOutput.collectAsStateWithLifecycle()
    val commonError by viewModel.dialogOutput.collectAsStateWithLifecycle()
    CashFlowTabView(
        uiState = uiState,
        commonError = commonError,
        onClickClose = onClickClose,
        onSelectPlannedItem = {},
        onSelectNotPlannedItem = {},
        onClickAdd = {},
        onClickEditCurrentBalance = {},
    )
}

@Composable
private fun CashFlowTabView(
    uiState: CashFlowUiState,
    commonError: CommonError?,
    onClickClose: () -> Unit,
    onSelectPlannedItem: (Int) -> Unit,
    onSelectNotPlannedItem: (Int) -> Unit,
    onClickAdd: () -> Unit,
    onClickEditCurrentBalance: () -> Unit,
) {
    when (uiState) {
        is CashFlowUiState.Loaded -> TODO()
        CashFlowUiState.Empty -> CashFlowEmptyVariant(
            onClickSetupBudget = onClickAdd,
        )
    }
}
