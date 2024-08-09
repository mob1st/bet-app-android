package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Get the budget builder for the given step.
 * @param categoryRepository The repository to fetch the categories.
 */
internal class GetBudgetBuilderForStepUseCase(
    private val categoryRepository: CategoryRepository,
) {
    operator fun get(
        step: BudgetBuilderAction.Step,
    ): Flow<BudgetBuilder> {
        return categoryRepository.getByIsExpenseAndRecurrencesType(
            isExpense = step.isExpense,
            recurrenceType = step.type,
        ).map { categories ->
            BudgetBuilder(step, categories)
        }
    }
}
