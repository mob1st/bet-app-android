package br.com.mob1st.features.finances.impl.fakes

import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItemGroup
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import br.com.mob1st.features.finances.publicapi.domain.usecases.GetFixedExpensesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull

class FakeGetFixedExpensesUseCase(
    val invokeState: MutableStateFlow<BudgetItemGroup<RecurrentCategory>?> = MutableStateFlow(null),
) : GetFixedExpensesUseCase {
    override fun invoke(): Flow<BudgetItemGroup<RecurrentCategory>> {
        return invokeState.mapNotNull { it }
    }
}
