package br.com.mob1st.features.finances.impl.ui.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.flows.stateInWhileSubscribed
import br.com.mob1st.core.state.contracts.UiStateOutputManager
import br.com.mob1st.core.state.managers.DialogDelegate
import br.com.mob1st.core.state.managers.DialogManager
import br.com.mob1st.core.state.managers.mapCatching
import br.com.mob1st.features.finances.impl.domain.usecases.GetCashFlowUseCase
import br.com.mob1st.features.utils.errors.CommonError
import kotlinx.coroutines.flow.StateFlow

class CashFlowViewModel(
    private val getCashFlowUseCase: GetCashFlowUseCase,
    private val stateHolder: CashFlowUiStateHolder,
) : ViewModel(),
    UiStateOutputManager<CashFlowUiState>,
    DialogManager<CommonError> by DialogDelegate() {
    override val uiStateOutput: StateFlow<CashFlowUiState> = getTransactions()
        .stateInWhileSubscribed(
            scope = viewModelScope,
            initialValue = CashFlowUiState.Empty,
        )

    private fun getTransactions() =
        getCashFlowUseCase()
            .mapCatching(
                map = stateHolder::asUiState,
            )
}
