package br.com.mob1st.features.finances.publicapi.domain.usecases

import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItemGroup
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import kotlinx.coroutines.flow.Flow

/**
 * Get the fixed expenses step content for the field selected in the builder
 */
interface GetFixedExpensesUseCase {
    /**
     * Invoke the use case
     */
    operator fun invoke(): Flow<BudgetItemGroup<RecurrentCategory>>
}
