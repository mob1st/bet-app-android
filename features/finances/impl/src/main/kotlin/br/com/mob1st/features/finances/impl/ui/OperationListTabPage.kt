package br.com.mob1st.features.finances.impl.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.features.utils.errors.CommonError
import org.koin.androidx.compose.koinViewModel

@Composable
fun TransactionListTabPage(onClickClose: () -> Unit) {
    val viewModel = koinViewModel<OperationListViewModel>()
    val uiState by viewModel.uiOutput.collectAsStateWithLifecycle()
    val commonError by viewModel.dialogOutput.collectAsStateWithLifecycle()
    TransactionListTabView(
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
private fun TransactionListTabView(
    uiState: TransactionListUiState,
    commonError: CommonError?,
    onClickClose: () -> Unit,
    onSelectPlannedItem: (Int) -> Unit,
    onSelectNotPlannedItem: (Int) -> Unit,
    onClickAdd: () -> Unit,
    onClickEditCurrentBalance: () -> Unit,
) {
    when (uiState) {
        is TransactionListUiState.Loaded -> TODO()
        TransactionListUiState.Empty -> TransactionListTabEmptyVariant(
            onClickSetupBudget = onClickAdd,
        )
    }
}
