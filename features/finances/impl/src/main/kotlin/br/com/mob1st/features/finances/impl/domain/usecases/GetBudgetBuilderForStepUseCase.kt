package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.repositories.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Get the budget builder for the given step.
 * @param categoryRepository The repository to fetch the categories.
 */
internal class GetBudgetBuilderForStepUseCase(
    private val categoryRepository: CategoriesRepository,
) {
    operator fun get(
        step: BuilderNextAction.Step,
    ): Flow<BudgetBuilder> {
        return categoryRepository.getByIsExpenseAndRecurrencesType(
            isExpense = step.isExpense,
            recurrenceType = step.type,
        ).map { categories ->
            BudgetBuilder(step, categories)
        }
    }
}
