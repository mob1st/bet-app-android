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
     * The output of the modals to be shown in the UI.
     */
    val dialogOutput: StateFlow<T?>

    /**
     * Shows a modal in the UI.
     */
    context(ViewModel)
    fun showModal(modalState: T)

    /**
     * Consumes the modals, removing them from the UI.
     */
    fun consumeDialog()
}

class DialogDelegate<T> : DialogManager<T> {
    private val _dialogOutput: MutableStateFlow<T?> = MutableStateFlow(null)
    override val dialogOutput: StateFlow<T?> = _dialogOutput.asStateFlow()

    override fun consumeDialog() {
        _dialogOutput.value = null
    }

    context(ViewModel)
    override fun showModal(modalState: T) {
        _dialogOutput.value = modalState
    }
}
