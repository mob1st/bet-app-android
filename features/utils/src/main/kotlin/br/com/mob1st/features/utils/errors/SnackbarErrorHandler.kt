package br.com.mob1st.features.utils.errors

import br.com.mob1st.core.state.managers.ErrorHandler
import br.com.mob1st.core.state.managers.SnackbarDelegate
import br.com.mob1st.core.state.managers.SnackbarManager

/**
 *  An error handler that shows Snackbars for [CommonError].
 */
class SnackbarErrorHandler(
    private val delegate: SnackbarDelegate<CommonError> = SnackbarDelegate(),
) : ErrorHandler(), SnackbarManager<CommonError> by delegate {
    override fun catch(throwable: Throwable) {
        super.catch(throwable)
        delegate.value = throwable.toCommonError()
    }
}
