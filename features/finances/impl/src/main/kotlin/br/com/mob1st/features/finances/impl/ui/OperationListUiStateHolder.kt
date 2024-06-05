package br.com.mob1st.features.finances.impl.ui

import br.com.mob1st.features.finances.impl.domain.entities.TransactionList

class OperationListUiStateHolder {
    private lateinit var state: TransactionList

    fun asUiState(state: TransactionList): TransactionListUiState {
        return TransactionListUiState.Empty
    }
}
