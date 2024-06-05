package br.com.mob1st.features.finances.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.flows.stateInRetained
import br.com.mob1st.core.state.contracts.UiStateManager
import br.com.mob1st.core.state.managers.DialogDelegate
import br.com.mob1st.core.state.managers.DialogManager
import br.com.mob1st.core.state.managers.mapCatching
import br.com.mob1st.features.finances.impl.domain.usecases.GetOperationListUseCase
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.features.utils.errors.dialogErrorHandler
import kotlinx.coroutines.flow.StateFlow

class OperationListViewModel(
    private val getOperationListUseCase: GetOperationListUseCase,
    private val stateHolder: OperationListUiStateHolder,
) : ViewModel(),
    UiStateManager<TransactionListUiState>,
    DialogManager<CommonError> by DialogDelegate() {
    override val uiOutput: StateFlow<TransactionListUiState> = getTransactions()
        .stateInRetained(
            scope = viewModelScope,
            initialValue = TransactionListUiState.Empty,
        )

    private fun getTransactions() =
        getOperationListUseCase()
            .mapCatching(
                map = stateHolder::asUiState,
                errorHandler = dialogErrorHandler,
            )
}
