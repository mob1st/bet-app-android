package br.com.mob1st.features.utils.errors

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.Text
import br.com.mob1st.core.design.molecules.buttons.ButtonState
import br.com.mob1st.core.design.organisms.helper.HelperState
import br.com.mob1st.core.design.organisms.snack.SnackState
import br.com.mob1st.core.kotlinx.errors.NoConnectivityException
import br.com.mob1st.features.utils.R
import br.com.mob1st.features.utils.errors.CommonError.NoConnectivity
import br.com.mob1st.features.utils.errors.CommonError.Unknown

/**
 * Common errors that are usually handled by all features.
 *
 * They are the type of errors that shows some kind of helper to the user and a predefined action, doesn't matter which
 * feature is being used.
 *
 * It's a sealed class with 2 implementations:
 * - [Unknown] for unknown errors
 * - [NoConnectivity] for no connectivity errors
 */
@Immutable
sealed interface CommonError {

    /**
     * The default snack state for this error
     */
    val snack: SnackState

    /**
     * The default helper state for this error
     */
    val helper: HelperState

    /**
     * Represents an unknown error
     */
    data object Unknown : CommonError {
        override val snack: SnackState = SnackState(
            supporting = Text(R.string.utils_commonerror_snackbar_unknown_supporting),
            action = null
        )
        override val helper: HelperState = HelperState(
            title = Text(R.string.utils_commonerror_helper_unknown_title),
            subtitle = Text(R.string.utils_commonerror_helper_unknown_subtitle),
            buttonState = ButtonState(
                text = Text(R.string.utils_commonerror_helper_unknown_button)
            )
        )
    }

    /**
     * Represents a no connectivity error
     */
    data object NoConnectivity : CommonError {
        override val snack: SnackState = SnackState(
            supporting = Text(R.string.utils_commonerror_snackbar_noconnectivity_supporting),
            action = Text(R.string.utils_commonerror_snackbar_noconnectivity_action)
        )

        override val helper: HelperState = HelperState(
            title = Text(R.string.utils_commonerror_helper_noconnectivity_title),
            subtitle = Text(R.string.utils_commonerror_helper_noconnectivity_subtitle),
            buttonState = ButtonState(
                text = Text(R.string.utils_commonerror_helper_noconnectivity_button)
            )
        )
    }

    companion object {

        /**
         * Converts a [Throwable] into a [CommonError]
         */
        fun from(throwable: Throwable): CommonError {
            return when (throwable) {
                is NoConnectivityException -> NoConnectivity
                else -> Unknown
            }
        }
    }
}
