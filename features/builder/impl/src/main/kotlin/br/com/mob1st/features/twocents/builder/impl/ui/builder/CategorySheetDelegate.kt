package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.lifecycle.ViewModel
import br.com.mob1st.core.state.managers.SheetManager
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.plus
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update

/**
 * Helper typealias to represent a suggestion update.
 * The [Pair.first] is the position of the suggestion to be updated and the [Pair.second] is the new value.
 */
private typealias SuggestionUpdate = Pair<Int, BuilderUiState.ListItem>

internal class CategorySheetDelegate : SheetManager<CategorySheet> {
    private val _manuallyAddedItemsState = MutableStateFlow<PersistentList<BuilderUiState.ListItem>>(
        persistentListOf(),
    )

    /**
     * The manually added items state.
     * It holds the entire list of manually added items, emitting a new value every time this list has some item added
     * or updated.
     */
    val manuallyAddedItemsState = _manuallyAddedItemsState.asStateFlow()

    private val _suggestionUpdateInput = MutableSharedFlow<SuggestionUpdate>()

    /**
     * The suggestion update input.
     * It emits a new item every time a suggestion is updated.
     * It can be combined with the flow that provides the list of suggestions.
     */
    val suggestionUpdateInput = _suggestionUpdateInput.asSharedFlow()

    private val _sheetState = MutableStateFlow<CategorySheet?>(null)

    /**
     * The sheet state.
     */
    val sheetState = _sheetState.asStateFlow()

    context(ViewModel)
    override fun showSheet(sheetState: CategorySheet) {
        _sheetState.value = sheetState
    }

    /**
     * Updates the category with the values from the keyboard.
     * @throws IllegalStateException if the keyboard is not shown when calling this method.
     */
    suspend fun updateCategory() {
        val keyboard = checkNotNull(_sheetState.getAndUpdate { null })
        val item = BuilderUiState.ListItem(
            name = keyboard.category,
            amount = keyboard.amount,
        )
        when (val operation = keyboard.operation) {
            CategorySheet.Operation.Add -> _manuallyAddedItemsState.update { list -> list + item }
            is CategorySheet.Operation.Update -> operation.update(item)
        }
    }

    override fun consumeSheet() {
        _sheetState.value = null
    }

    private suspend fun CategorySheet.Operation.Update.update(
        item: BuilderUiState.ListItem,
    ) {
        if (isSuggestion) {
            _suggestionUpdateInput.emit(position to item)
        } else {
            _manuallyAddedItemsState.update { list -> list.set(position, item) }
        }
    }
}
