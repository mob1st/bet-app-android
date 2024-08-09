package br.com.mob1st.features.finances.impl.domain.infra.repositories

import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import kotlinx.coroutines.flow.Flow

/**
 * Repository for the Suggestion aggregate.
 */
internal interface CategorySuggestionRepository {
    /**
     * Returns the suggestions available for the given [step].
     * If a writes happens in a category that is linked to a suggestion returned by this method, then a new emission
     * will be triggered in the flow.
     * @param step The step of the category builder.
     * @return A flow of the suggestions.
     * @see CategoryRepository to check how to execute write operations in the categories.
     */
    fun getByStep(
        step: BudgetBuilderAction.Step,
    ): Flow<List<CategorySuggestion>>
}
