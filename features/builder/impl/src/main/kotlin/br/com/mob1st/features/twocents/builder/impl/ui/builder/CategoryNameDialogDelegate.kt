package br.com.mob1st.features.twocents.builder.impl.ui.builder

import br.com.mob1st.core.state.managers.DialogDelegate
import br.com.mob1st.core.state.managers.DialogManager
import br.com.mob1st.core.state.managers.ErrorHandler
import br.com.mob1st.core.state.managers.catching
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update

/**
 * Manage the custom dialog that displays the category name.
 */
internal interface CategoryNameDialogManager : DialogManager<CategoryNameDialogState> {
    /**
     * Opens the dialog to fill the category name and starts the creation of a new manual category.
     */
    fun setCategoryName(name: String)

    /**
     * Opens the dialog to fill the category name and starts the creation of a new manual category.
     */
    fun showCategoryNameDialog()

    /**
     * Dismiss the dialog and proceed to the next step of the category creation.
     */
    fun submitCategoryName()
}

/**
 * Manages the state of the dialog that displays the category name.
 * @property errorHandler Cascade errors to the error handler. It's usually the ViewModel error handler.
 * @property delegate The dialog delegate that manages the dialog state.
 */
internal class CategoryNameDialogDelegate(
    private val errorHandler: ErrorHandler,
    private val delegate: DialogDelegate<CategoryNameDialogState> = DialogDelegate(),
) : CategoryNameDialogManager, DialogManager<CategoryNameDialogState> by delegate {
    private val _nameInput = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val nameInput = _nameInput.asSharedFlow()

    override fun submitCategoryName() = errorHandler.catching {
        val name = checkNotNull(delegate.getAndUpdate { null }).text
        _nameInput.tryEmit(name)
    }

    override fun setCategoryName(name: String) = errorHandler.catching {
        delegate.update { dialog ->
            checkNotNull(dialog).copy(text = name)
        }
    }

    override fun showCategoryNameDialog() {
        delegate.value = CategoryNameDialogState()
    }
}
