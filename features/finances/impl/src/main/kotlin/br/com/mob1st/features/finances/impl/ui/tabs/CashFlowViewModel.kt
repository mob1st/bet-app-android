package br.com.mob1st.features.finances.impl.ui.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.flows.stateInRetained
import br.com.mob1st.core.state.contracts.UiStateManager
import br.com.mob1st.core.state.managers.DialogDelegate
import br.com.mob1st.core.state.managers.DialogManager
import br.com.mob1st.core.state.managers.mapCatching
import br.com.mob1st.features.finances.impl.domain.usecases.GetCashFlowUseCase
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.features.utils.errors.dialogErrorHandler
import kotlinx.coroutines.flow.StateFlow

class CashFlowViewModel(
    private val getCashFlowUseCase: GetCashFlowUseCase,
    private val stateHolder: CashFlowUiStateHolder,
) : ViewModel(),
    UiStateManager<CashFlowUiState>,
    DialogManager<CommonError> by DialogDelegate() {
    override val uiOutput: StateFlow<CashFlowUiState> = getTransactions()
        .stateInRetained(
            scope = viewModelScope,
            initialValue = CashFlowUiState.Empty,
        )

    private fun getTransactions() =
        getCashFlowUseCase()
            .mapCatching(
                map = stateHolder::asUiState,
                errorHandler = dialogErrorHandler,
            )
}
