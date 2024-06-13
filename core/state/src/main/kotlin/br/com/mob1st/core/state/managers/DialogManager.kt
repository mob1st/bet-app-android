package br.com.mob1st.core.state.managers

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages the modals to be shown in the UI and allow the consumption of them.
 */
interface DialogManager<T> {
    /**
     * Handy method for ViewModels to directly show the modals.
     */
    context(ViewModel)
    fun showDialog(dialog: T)

    /**
     * The output of the modals to be shown in the UI.
     */
    val dialogOutput: StateFlow<T?>

    /**
     * Consumes the modals, removing them from the UI.
     */
    fun consumeDialog()
}

class DialogDelegate<T> : DialogManager<T>, MutableStateFlow<T?> by MutableStateFlow(null) {
    override val dialogOutput: StateFlow<T?> = asStateFlow()

    context(ViewModel)
    override fun showDialog(dialog: T) {
        value = dialog
    }

    override fun consumeDialog() {
        value = null
    }
}
