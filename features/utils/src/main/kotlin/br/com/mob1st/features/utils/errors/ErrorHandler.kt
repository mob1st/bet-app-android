package br.com.mob1st.features.utils.errors

import androidx.lifecycle.ViewModel
import br.com.mob1st.core.state.managers.DialogManager
import br.com.mob1st.core.state.managers.ErrorHandler
import br.com.mob1st.core.state.managers.SnackbarManager

/**
 * Displays a dialog with the common error message.
 */
context(ViewModel)
val DialogManager<CommonError>.dialogErrorHandler: ErrorHandler
    get() = ErrorHandler { showModal(CommonError.Unknown) }

/**
 * Displays a snackbar with the common error message.
 */
context(ViewModel)
val SnackbarManager<CommonError>.snackbarErrorHandler: ErrorHandler
    get() = ErrorHandler { showSnackbar(CommonError.Unknown) }
