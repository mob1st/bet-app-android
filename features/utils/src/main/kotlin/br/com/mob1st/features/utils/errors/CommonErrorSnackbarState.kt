package br.com.mob1st.features.utils.errors

import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import br.com.mob1st.core.design.organisms.snack.SnackbarState
import br.com.mob1st.core.design.organisms.snack.snackbar
import br.com.mob1st.core.state.managers.ErrorHandler
import br.com.mob1st.features.utils.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Presents a [CommonError] as a snackbar.
 * @property commonError The [CommonError] to be presented.
 */
@Immutable
data class CommonErrorSnackbarState(
    val commonError: CommonError,
) : SnackbarState {
    /**
     * Creates a [CommonErrorSnackbarState] from a [Throwable].
     * @param throwable The throwable to be presented.
     * @see Throwable.toCommonError
     */
    constructor(throwable: Throwable) : this(throwable.toCommonError())

    @Composable
    override fun resolve(): SnackbarVisuals {
        return commonError.toSnackbarVisuals()
    }
}

/**
 * Converts a [CommonError] to a [SnackbarVisuals].
 * @return The [SnackbarVisuals] representing the [CommonError].
 */
@Composable
fun CommonError.toSnackbarVisuals(): SnackbarVisuals {
    val text = stringResource(id = R.string.utils_commonerror_snackbar_unknown_supporting)
    return remember(this) {
        snackbar(message = text)
    }
}

/**
 * Utility function to create an [ErrorHandler] that updates the state with a [CommonErrorSnackbarState].
 */
fun <State> MutableStateFlow<State>.commonErrorHandler(
    updateFailure: State.(CommonErrorSnackbarState) -> State,
): ErrorHandler {
    return ErrorHandler { failure ->
        update {
            it.updateFailure(CommonErrorSnackbarState(failure))
        }
    }
}
