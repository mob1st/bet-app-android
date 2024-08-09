package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.toDefaultRecurrences
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CategoryRepository
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CategorySuggestionRepository
import kotlinx.coroutines.flow.first

/**
 * Starts the builder step by preloading the categories if suggestions if needed.
 * @param categoryRepository The repository to store the categories.
 * @param categorySuggestionRepository The repository that provides the suggestions to be transformed into categories.
 */
internal class StartBuilderStepUseCase(
    private val categoryRepository: CategoryRepository,
    private val categorySuggestionRepository: CategorySuggestionRepository,
) {
    /**
     * Prefills the categories for the given [step] if it didn't have any.
     * @param step The step to start the builder.
     */
    suspend operator fun invoke(
        step: BudgetBuilderAction.Step,
    ) {
        val count = categoryRepository.countByIsExpenseAndRecurrencesType(
            isExpense = step.isExpense,
            recurrenceType = step.type,
        ).first()
        if (count == 0L) {
            val suggestions = categorySuggestionRepository.getByStep(step).first()
            val categories = suggestions.map { suggestion ->
                Category(
                    isSuggested = true,
                    name = suggestion.name,
                    image = suggestion.image,
                    recurrences = step.type.toDefaultRecurrences(),
                    isExpense = step.isExpense,
                )
            }
            categoryRepository.addAll(categories)
        }
    }
}
