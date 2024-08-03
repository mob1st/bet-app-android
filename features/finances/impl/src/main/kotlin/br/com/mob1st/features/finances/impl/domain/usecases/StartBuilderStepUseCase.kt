package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.repositories.CategoriesRepository
import br.com.mob1st.features.finances.impl.domain.repositories.CategorySuggestionRepository
import kotlinx.coroutines.flow.first

/**
 * Starts the builder step by preloading the categories if suggestions if needed.
 * @param categoriesRepository The repository to store the categories.
 * @param categorySuggestionRepository The repository that provides the suggestions to be transformed into categories.
 * @param categoryFactory The factory to create the categories from a suggestion for a given step.
 */
internal class StartBuilderStepUseCase(
    private val categoriesRepository: CategoriesRepository,
    private val categorySuggestionRepository: CategorySuggestionRepository,
    private val categoryFactory: Category.Factory,
) {
    /**
     * Prefills the categories for the given [step] if it didn't have any.
     * @param step The step to start the builder.
     */
    suspend operator fun invoke(
        step: BuilderNextAction.Step,
    ) {
        val count = categoriesRepository.countByIsExpenseAndRecurrencesType(
            isExpense = step.isExpense,
            recurrenceType = step.type,
        ).first()
        if (count == 0L) {
            val suggestions = categorySuggestionRepository.getByStep(step).first()
            val categories = suggestions.map { suggestion ->
                categoryFactory.create(step, suggestion)
            }
            categoriesRepository.addAll(categories)
        }
    }
}
