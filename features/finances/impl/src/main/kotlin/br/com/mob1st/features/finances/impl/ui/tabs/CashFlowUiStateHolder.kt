package br.com.mob1st.features.finances.impl.ui.tabs

import br.com.mob1st.features.finances.impl.domain.entities.TransactionList

class CashFlowUiStateHolder {
    private lateinit var state: TransactionList

    fun asUiState(state: TransactionList): CashFlowUiState {
        return CashFlowUiState.Empty
    }
}
