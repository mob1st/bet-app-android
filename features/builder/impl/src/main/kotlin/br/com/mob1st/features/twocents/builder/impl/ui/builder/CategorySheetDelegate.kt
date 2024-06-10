package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.lifecycle.ViewModel
import br.com.mob1st.core.state.managers.SheetManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate

internal class CategorySheetDelegate : SheetManager<CategorySheet> {
    private val _sheetOutput = MutableStateFlow<CategorySheet?>(null)
    override val sheetOutput = _sheetOutput.asStateFlow()

    val manualItemUpdateInput = MutableSharedFlow<ItemUpdate>()
    val suggestedItemUpdateInput = MutableSharedFlow<ItemUpdate>()

    context(ViewModel)
    override fun showSheet(sheetState: CategorySheet) {
        _sheetOutput.value = sheetState
    }

    override fun consumeSheet() {
        _sheetOutput.value = null
    }

    /**
     * Updates the category with the values from the keyboard.
     * @throws IllegalStateException if the keyboard is not shown when calling this method.
     */
    suspend fun updateCategory() {
        val keyboard = checkNotNull(_sheetOutput.getAndUpdate { null })
        val item = BuilderUiState.ListItem(
            name = keyboard.category,
            amount = keyboard.amount,
        )
        when (val operation = keyboard.operation) {
            CategorySheet.Operation.Add -> manualItemUpdateInput.emit(ItemUpdate(operation, item))
            is CategorySheet.Operation.Update -> operation.update(item)
        }
    }

    private suspend fun CategorySheet.Operation.Update.update(
        item: BuilderUiState.ListItem,
    ) {
        if (isSuggestion) {
            suggestedItemUpdateInput.emit(ItemUpdate(this, item))
        } else {
            manualItemUpdateInput.emit(ItemUpdate(this, item))
        }
    }
}
