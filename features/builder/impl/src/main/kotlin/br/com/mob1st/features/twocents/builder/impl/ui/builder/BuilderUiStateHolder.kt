package br.com.mob1st.features.twocents.builder.impl.ui.builder

import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategoryBatch
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategorySuggestion

internal class BuilderUiStateHolder(
    val recurrenceType: RecurrentCategory,
) {
    lateinit var suggestions: List<CategorySuggestion>

    fun suggestionAsState(
        suggestions: List<CategorySuggestion>,
        userInput: BuilderUserInput,
    ): List<BuilderUiState.ListItem> {
        this.suggestions = suggestions
        return userInput.suggested.toSuggestedListItem(suggestions)
    }

    fun createBatch(): CategoryBatch {
        TODO()
    }
}
