package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.repositories.CategoriesRepository
import br.com.mob1st.features.finances.impl.domain.repositories.CategorySuggestionRepository
import kotlinx.coroutines.flow.first

internal class ProceedInBuilderUseCase(
    private val categoriesRepository: CategoriesRepository,
    private val categorySuggestionRepository: CategorySuggestionRepository,
    private val categoryFactory: Category.Factory,
) {
    suspend operator fun invoke(
        step: BuilderNextAction.Step,
    ) {
        val count = categoriesRepository.countByIsExpenseAndRecurrencesType(
            isExpense = step.isExpense,
            recurrenceType = step.type,
        )
        if (count == 0L) {
            val suggestions = categorySuggestionRepository.getByStep(step).first()
            val categories = suggestions.map { suggestion ->
                categoryFactory.create(step, suggestion)
            }
            categoriesRepository.addAll(categories)
        }
    }
}
