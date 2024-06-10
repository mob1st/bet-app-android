package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.lifecycle.ViewModel
import br.com.mob1st.core.state.managers.SheetManager
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.plus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update

/**
 * Helper typealias to represent a suggestion update.
 * The [Pair.first] is the position of the suggestion to be updated and the [Pair.second] is the new value.
 */
private typealias SuggestionUpdate = Pair<Int, BuilderUiState.ListItem>

internal class CategorySheetDelegate : SheetManager<CategorySheet> {
    private lateinit var manuallyAddedItemsState: MutableStateFlow<PersistentList<BuilderUiState.ListItem>>

    /**
     * The manually added items state.
     * It holds the entire list of manually added items, emitting a new value every time this list has some item added
     * or updated.
     */
    fun manuallyAddedItemsState(
        initialValue: PersistentList<BuilderUiState.ListItem>,
    ): StateFlow<PersistentList<BuilderUiState.ListItem>> {
        manuallyAddedItemsState = MutableStateFlow(initialValue)
        return manuallyAddedItemsState.asStateFlow()
    }

    private val _sheetState = MutableStateFlow<CategorySheet?>(null)

    /**
     * The sheet state.
     */
    val sheetState = _sheetState.asStateFlow()

    private val suggestionUpdateInput = MutableSharedFlow<SuggestionUpdate>()

    context(ViewModel)
    override fun showSheet(sheetState: CategorySheet) {
        _sheetState.value = sheetState
    }

    override fun consumeSheet() {
        _sheetState.value = null
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
            CategorySheet.Operation.Add -> manuallyAddedItemsState.update { list -> list + item }
            is CategorySheet.Operation.Update -> operation.update(item)
        }
    }

    /**
     * Combines the initial suggestions with the updates from the user.
     *
     * Despite the prefix combine in the function name, it doesn't wait for update emissions to start emitting the
     * suggestions. The suggestions are emitted first and then the updates are combined to the suggestions, resulting in
     * new list items containing the updates provided by the user.
     * @param source The flow of the suggestions.
     * @return The flow of the suggestions with the updates applied.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun combineSuggestionsAndUpdates(
        source: Flow<PersistentList<BuilderUiState.ListItem>>,
    ) = source.transformLatest { items ->
        /*
        Emit an initial value because the combine operator requires emissions from all flows to start emitting.
        This way we can display the initial list of suggestion first and then apply the updates.
         */
        emit(items)
        emitAll(
            source.combine(suggestionUpdateInput) { suggestions, (position, item) ->
                suggestions.set(position, item)
            },
        )
    }

    private suspend fun CategorySheet.Operation.Update.update(
        item: BuilderUiState.ListItem,
    ) {
        if (isSuggestion) {
            suggestionUpdateInput.emit(position to item)
        } else {
            manuallyAddedItemsState.update { list -> list.set(position, item) }
        }
    }
}
