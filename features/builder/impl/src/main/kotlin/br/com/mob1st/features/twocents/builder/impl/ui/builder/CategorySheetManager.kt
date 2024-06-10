package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.lifecycle.ViewModel
import br.com.mob1st.core.state.managers.SheetManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update

/**
 * A extension of [SheetManager] that provides additional methods to handle a [CategorySheetState].
 */
internal interface CategorySheetManager : SheetManager<CategorySheetState> {
    /**
     * Updates the category with the data filled in the [CategorySheetState].
     */
    fun submitCategory()

    /**
     * Sets the amount of the category.
     * @param amount the amount to be set.
     */
    fun setCategoryAmount(amount: String)
}

internal class CategorySheetDelegate : CategorySheetManager {
    private val _sheetOutput = MutableStateFlow<CategorySheetState?>(null)
    override val sheetOutput = _sheetOutput.asStateFlow()

    val manualItemUpdateInput = MutableSharedFlow<ItemUpdate>()
    val suggestedItemUpdateInput = MutableSharedFlow<ItemUpdate>()

    context(ViewModel)
    override fun showSheet(sheetState: CategorySheetState) {
        _sheetOutput.value = sheetState
    }

    override fun consumeSheet() {
        _sheetOutput.value = null
    }

    /**
     * Updates the category with the values from the keyboard.
     * @throws IllegalStateException if the keyboard is not shown when calling this method.
     */
    override fun submitCategory() {
        val keyboard = checkNotNull(_sheetOutput.getAndUpdate { null })
        val item = BuilderUiState.ListItem(
            name = keyboard.category,
            amount = keyboard.amount,
        )
        when (val operation = keyboard.operation) {
            CategorySheetState.Operation.Add -> manualItemUpdateInput.tryEmit(ItemUpdate(operation, item))
            is CategorySheetState.Operation.Update -> operation.update(item)
        }
    }

    override fun setCategoryAmount(amount: String) {
        _sheetOutput.update {
            checkNotNull(it).copy(amount = amount)
        }
    }

    private fun CategorySheetState.Operation.Update.update(
        item: BuilderUiState.ListItem,
    ) {
        if (isSuggestion) {
            suggestedItemUpdateInput.tryEmit(ItemUpdate(this, item))
        } else {
            manualItemUpdateInput.tryEmit(ItemUpdate(this, item))
        }
    }
}
