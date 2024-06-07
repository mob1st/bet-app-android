package br.com.mob1st.features.twocents.builder.impl.ui.builder

import br.com.mob1st.core.database.RecurrenceType
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategoryBatch
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategorySuggestion

internal class BuilderUiStateHolder(
    val recurrenceType: RecurrenceType,
    private val uiStateFactory: BuilderUiStateFactory,
    private val categoryBatchFactory: BuilderCategoryBatchFactory,
) {
    private lateinit var suggestions: List<CategorySuggestion>
    private lateinit var input: BuilderUserInput

    fun asUiState(
        suggestions: List<CategorySuggestion>,
        input: BuilderUserInput,
    ): BuilderUiState {
        this.suggestions = suggestions
        this.input = input
        return uiStateFactory.create(
            suggestions,
            input,
        )
    }

    fun getSuggestionId(position: Int) = suggestions[position].id

    fun createBatch(): CategoryBatch {
        return categoryBatchFactory.create(
            suggestions,
            input,
        )
    }
}
