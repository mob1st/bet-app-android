package br.com.mob1st.features.utils.uimessages

import br.com.mob1st.core.design.organisms.ModalState
import br.com.mob1st.core.design.organisms.snack.SnackbarState
import br.com.mob1st.features.utils.errors.CommonError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

/**
 * Manages the common UI messages like snackbars and modals.
 * It also has handy methods to catch exceptions and transform them into snackbars or modals.
 * It's useful to be used in ViewModels.
 */
interface UiMessageDelegate : SnackbarDelegate, ModalDelegate {
    /**
     * Returns a [CoroutineExceptionHandler] that will catch exceptions and transform them into snackbars, mapping them
     * to [CommonError].
     * This is useful to be used as a Coroutine Context launched by scopes.
     */
    fun catchAsSnackbar(): CoroutineExceptionHandler

    /**
     * Catch the given [throwable] and transform it into a snackbar, mapping it to [CommonError].
     * It's useful to be used in try-catch blocks.
     */
    fun catchAsSnackbar(throwable: Throwable)

    /**
     * Returns a [CoroutineExceptionHandler] that will catch exceptions and transform them into modals, mapping them
     * to [CommonError].
     * This is useful to be used as a Coroutine Context launched by scopes.
     */
    fun catchAsModal(isDismissible: Boolean = true): CoroutineExceptionHandler

    /**
     * Catch the given [throwable] and transform it into a modal, mapping it to [CommonError].
     * It's useful to be used in try-catch blocks.
     * @param throwable the exception to be caught.
     * @param isDismissible if the modal should be dismissible or not.
     * @see CommonError
     */
    fun catchAsModal(
        throwable: Throwable,
        isDismissible: Boolean = false,
    )
}

/**
 * Default implementation of [UiMessageDelegate].
 * @param snackbarOutput the output of the snackbars to be shown in the UI.
 * @param modalOutput the output of the modals to be shown in the UI.
 */
fun UiMessageDelegate(
    snackbarOutput: MutableStateFlow<SnackbarState?> = MutableStateFlow(null),
    modalOutput: MutableStateFlow<ModalState?> = MutableStateFlow(null),
): UiMessageDelegate =
    UiMessageDelegateImpl(
        _snackbarOutput = snackbarOutput,
        _modalOutput = modalOutput,
    )

private class UiMessageDelegateImpl(
    private val _snackbarOutput: MutableStateFlow<SnackbarState?>,
    private val _modalOutput: MutableStateFlow<ModalState?>,
) : UiMessageDelegate,
    SnackbarDelegate by SnackbarDelegate(_snackbarOutput),
    ModalDelegate by ModalDelegate(_modalOutput) {
    override val snackbarOutput: StateFlow<SnackbarState?> = _snackbarOutput.asStateFlow()
    override val modalOutput: StateFlow<ModalState?> = _modalOutput.asStateFlow()

    override fun catchAsModal(isDismissible: Boolean): CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            catchAsModal(throwable, isDismissible)
        }

    override fun catchAsSnackbar(): CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            catchAsSnackbar(throwable)
        }

    override fun catchAsModal(
        throwable: Throwable,
        isDismissible: Boolean,
    ) {
        showModal(throwable.toCommonError().modal(isDismissible))
    }

    override fun catchAsSnackbar(throwable: Throwable) {
        showSnackbar(throwable.toCommonError().snack())
    }

    private fun Throwable.toCommonError(): CommonError {
        Timber.e(this)
        return CommonError.of(this)
    }
}
