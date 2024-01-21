package br.com.mob1st.features.finances.impl.fakes

import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategoryGroup
import br.com.mob1st.features.finances.publicapi.domain.usecases.GetFixedExpensesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull

class FakeGetFixedExpensesUseCase(
    val invokeState: MutableStateFlow<RecurrentCategoryGroup<RecurrentCategory.Fixed>?> = MutableStateFlow(null),
) : GetFixedExpensesUseCase {
    override fun invoke(): Flow<RecurrentCategoryGroup<RecurrentCategory.Fixed>> {
        return invokeState.mapNotNull { it }
    }
}
