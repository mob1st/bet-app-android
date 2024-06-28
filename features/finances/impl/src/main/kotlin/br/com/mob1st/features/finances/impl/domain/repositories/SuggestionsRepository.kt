package br.com.mob1st.features.finances.impl.domain.repositories

import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import kotlinx.coroutines.flow.Flow

/**
 * Repository for the Suggestion aggregate.
 */
internal interface SuggestionsRepository {
    /**
     * Returns the suggestions available for the given [step].
     * If a writes happens in a category that is linked to a suggestion returned by this method, then a new emission
     * will be triggered in the flow.
     * @param step The step of the category builder.
     * @return A flow of the suggestions.
     * @see CategoriesRepository to check how to execute write operations in the categories.
     */
    fun getByTypeAndIsExpense(
        step: BuilderNextAction.Step,
    ): Flow<List<CategorySuggestion>>
}
