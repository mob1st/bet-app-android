package br.com.mob1st.features.utils.uimessages

import br.com.mob1st.core.design.organisms.snack.SnackbarState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Provides Snackbars to be shown in the UI and allow the consumption of them.
 */
interface SnackbarStateManager {
    /**
     * The output of the Snackbars to be shown in the UI.
     */
    val snackbarOutput: StateFlow<SnackbarState?>

    /**
     * Consumes the Snackbars.
     */
    fun consumeSnackbar()
}

/**
 * The delegation to be used by ViewModels to show Snackbars in the UI.
 */
interface SnackbarDelegate : SnackbarStateManager {
    fun showSnackbar(snackbarState: SnackbarState)
}

/**
 * Creates a [SnackbarDelegate] instance using the default implementation.
 */
fun SnackbarDelegate(snackbarOutput: MutableStateFlow<SnackbarState?> = MutableStateFlow(null)): SnackbarDelegate =
    SnackbarDelegateImpl(
        _snackbarOutput = snackbarOutput,
    )

private class SnackbarDelegateImpl(
    private val _snackbarOutput: MutableStateFlow<SnackbarState?>,
) : SnackbarDelegate {
    override val snackbarOutput: StateFlow<SnackbarState?> = _snackbarOutput.asStateFlow()

    override fun showSnackbar(snackbarState: SnackbarState) {
        _snackbarOutput.value = snackbarState
    }

    override fun consumeSnackbar() {
        _snackbarOutput.value = null
    }
}
