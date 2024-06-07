package br.com.mob1st.features.twocents.builder.impl.ui.builder

import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategorySuggestion
import kotlinx.collections.immutable.toImmutableList

internal fun interface BuilderUiStateFactory {
    fun create(
        suggestions: List<CategorySuggestion>,
        userInput: BuilderUserInput,
    ): BuilderUiState
}

internal fun BuilderUiStateFactory(): BuilderUiStateFactory {
    return BuilderUiStateFactoryImpl
}

private object BuilderUiStateFactoryImpl : BuilderUiStateFactory {
    override fun create(
        suggestions: List<CategorySuggestion>,
        userInput: BuilderUserInput,
    ): BuilderUiState {
        return BuilderUiState(
            manuallyAdded = userInput.manuallyAdded.map { entry ->
                BuilderUiState.ListItem(
                    name = TextState(entry.name),
                    amount = entry.amount,
                )
            }.toImmutableList(),
            suggested = suggestions.map { suggestion ->
                val entry = userInput.suggested[suggestion.id] ?: BuilderUserInput.Entry()
                BuilderUiState.ListItem(
                    name = TextState(suggestion.name.toResId()),
                    amount = entry.amount,
                )
            }.toImmutableList(),
        )
    }
}
