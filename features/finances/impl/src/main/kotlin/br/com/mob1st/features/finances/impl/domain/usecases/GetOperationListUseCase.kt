package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.features.finances.impl.domain.entities.TransactionList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetOperationListUseCase {
    operator fun invoke(): Flow<TransactionList> {
        return flowOf(
            TransactionList(listOf()),
        )
    }
}
