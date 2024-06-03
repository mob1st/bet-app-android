package br.com.mob1st.core.state.managers

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Provides Snackbars to be shown in the UI and allow the consumption of them.
 */
interface SnackbarManager<T> {
    /**
     * The output of the Snackbars to be shown in the UI.
     */
    val snackbarOutput: StateFlow<T?>

    /**
     * Shows a Snackbar in the UI.
     * @param snackbarState the Snackbar to be shown.
     */
    context(ViewModel)
    fun showSnackbar(snackbarState: T)

    /**
     * Consumes the Snackbars.
     */
    fun consumeSnackbar()

    /**
     * Performs the action of the Snackbar.
     */
    fun performSnackbarAction() = consumeSnackbar()
}

/**
 * The delegation to be used by ViewModels to show Snackbars in the UI.
 * @param T the type of the Snackbars to be shown.
 */
class SnackbarDelegate<T> : SnackbarManager<T> {
    private val _snackbarOutput = MutableStateFlow<T?>(null)
    override val snackbarOutput: StateFlow<T?> = _snackbarOutput.asStateFlow()

    override fun consumeSnackbar() {
        _snackbarOutput.value = null
    }

    context(ViewModel)
    override fun showSnackbar(snackbarState: T) {
        _snackbarOutput.value = snackbarState
    }
}
