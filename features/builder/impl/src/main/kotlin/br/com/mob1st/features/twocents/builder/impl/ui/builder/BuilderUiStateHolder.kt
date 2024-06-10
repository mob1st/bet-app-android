package br.com.mob1st.features.twocents.builder.impl.ui.builder

import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategoryBatch
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategorySuggestion
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

internal class BuilderUiStateHolder(
    val recurrenceType: RecurrentCategory,
) {
    lateinit var suggestions: List<CategorySuggestion>

    fun asUiState(
        suggestions: List<CategorySuggestion>,
        userInput: BuilderUserInput,
    ): PersistentList<BuilderUiState.ListItem> {
        this.suggestions = suggestions
        return suggestions.mapNotNull { suggestion ->
            userInput.suggested[suggestion.id]?.let { entry ->
                BuilderUiState.ListItem(
                    name = TextState(suggestion.name.toResId()),
                    amount = entry.amount,
                )
            }
        }.toPersistentList()
    }

    fun createBatch(): CategoryBatch {
        TODO()
    }
}
