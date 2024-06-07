package br.com.mob1st.features.twocents.builder.impl.ui.builder

import br.com.mob1st.core.database.RecurrenceType
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategoryBatch
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategorySuggestion
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

internal class BuilderUiStateHolder(
    val recurrenceType: RecurrenceType,
    private val categoryBatchFactory: BuilderCategoryBatchFactory,
) {
    lateinit var suggestions: List<CategorySuggestion>
    private lateinit var input: BuilderUserInput

    fun asUiState(
        suggestions: List<CategorySuggestion>,
        userInput: BuilderUserInput,
    ): PersistentList<BuilderUiState.ListItem<Int>> {
        this.suggestions = suggestions
        return suggestions.mapNotNull { suggestion ->
            userInput.suggested[suggestion.id]?.let { entry ->
                BuilderUiState.ListItem(
                    name = suggestion.name.toResId(),
                    amount = InputState(
                        value = entry.amount,
                    ),
                )
            }
        }.toPersistentList()
    }

    fun getSuggestionId(position: Int) = suggestions[position].id

    fun createBatch(): CategoryBatch {
        return categoryBatchFactory.create(
            suggestions,
            input,
        )
    }
}
