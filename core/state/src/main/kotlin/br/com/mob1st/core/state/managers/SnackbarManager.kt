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
     * Handy method for ViewModels to directly show the Snackbars.
     */
    context(ViewModel)
    fun showSnackbar(snackbar: T)

    /**
     * The output of the Snackbars to be shown in the UI.
     */
    val snackbarOutput: StateFlow<T?>

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
class SnackbarDelegate<T> : SnackbarManager<T>, MutableStateFlow<T?> by MutableStateFlow(null) {
    override val snackbarOutput: StateFlow<T?> = asStateFlow()

    context(ViewModel)
    override fun showSnackbar(snackbar: T) {
        value = snackbar
    }

    override fun consumeSnackbar() {
        value = null
    }
}
