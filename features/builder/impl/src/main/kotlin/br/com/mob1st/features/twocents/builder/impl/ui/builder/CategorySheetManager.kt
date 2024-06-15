package br.com.mob1st.features.twocents.builder.impl.ui.builder

import br.com.mob1st.core.state.managers.ErrorHandler
import br.com.mob1st.core.state.managers.SheetDelegate
import br.com.mob1st.core.state.managers.SheetManager
import br.com.mob1st.core.state.managers.catching
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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

/**
 * Manages the state of the category sheet.
 * @property errorHandler Cascade errors to the error handler. It's usually the ViewModel error handler.
 * @property sheetDelegate The sheet delegate that manages the sheet state.
 */
internal class CategorySheetDelegate(
    private val errorHandler: ErrorHandler,
    private val sheetDelegate: SheetDelegate<CategorySheetState> = SheetDelegate(),
) : CategorySheetManager, SheetManager<CategorySheetState> by sheetDelegate {
    private val _categorySheetInput = MutableSharedFlow<CategorySheetState>(extraBufferCapacity = 1)
    val categorySheetInput = _categorySheetInput.asSharedFlow()

    override fun submitCategory() = errorHandler.catching {
        val sheet = checkNotNull(sheetDelegate.getAndUpdate { null })
        _categorySheetInput.tryEmit(sheet)
    }

    override fun setCategoryAmount(amount: String) = errorHandler.catching {
        sheetDelegate.update { sheet ->
            checkNotNull(sheet).setAmount(amount)
        }
    }
}
