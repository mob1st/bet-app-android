package br.com.mob1st.features.twocents.builder.impl.ui.builder

import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.core.kotlinx.structures.Id
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategorySuggestion
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.plus
import kotlinx.collections.immutable.toPersistentList

/**
 * Maps the manual user input to the UI state.
 */
internal fun List<BuilderUserInput.Entry>.toManualListItem(): List<BuilderUiState.ListItem> {
    return map {
        BuilderUiState.ListItem(
            name = TextState(it.name),
            amount = it.amount,
        )
    }
}

/**
 * Maps the suggested user input to the UI state.
 * It uses the [suggestions] list to match the suggestions with the user input.
 */
internal fun Map<Id, BuilderUserInput.Entry>.toSuggestedListItem(
    suggestions: List<CategorySuggestion>,
): List<BuilderUiState.ListItem> {
    return suggestions.mapNotNull { suggestion ->
        get(suggestion.id)?.let { entry ->
            BuilderUiState.ListItem(
                name = TextState(suggestion.name.toResId()),
                amount = entry.amount,
            )
        }
    }.toPersistentList()
}

/**
 * Maps the UI state to the manual user input.
 */
internal fun List<BuilderUiState.ListItem>.toManualEntryList(): List<BuilderUserInput.Entry> {
    return map { item ->
        BuilderUserInput.Entry(
            name = item.name.toString(),
            amount = item.amount,
        )
    }
}

/**
 * Maps the UI state to the suggested user input.
 */
internal fun List<BuilderUiState.ListItem>.toSuggestedEntryMap(
    suggestions: List<CategorySuggestion>,
): Map<Id, BuilderUserInput.Entry> {
    return mapIndexed { index, item ->
        suggestions[index].id to BuilderUserInput.Entry(
            name = "",
            amount = item.amount,
        )
    }.toMap()
}

/**
 * Applies the given [itemUpdate] to the list
 * @param itemUpdate The update to be applied
 * @return the updated list
 */
internal fun PersistentList<BuilderUiState.ListItem>.applyUpdate(
    itemUpdate: ItemUpdate,
): PersistentList<BuilderUiState.ListItem> {
    return when (val operation = itemUpdate.operation) {
        CategorySheet.Operation.Add -> this + itemUpdate.newItem
        is CategorySheet.Operation.Update -> set(operation.position, itemUpdate.newItem)
    }
}
