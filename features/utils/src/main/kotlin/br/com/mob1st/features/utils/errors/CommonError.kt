package br.com.mob1st.features.utils.errors

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.Text
import br.com.mob1st.core.design.molecules.buttons.ButtonBar
import br.com.mob1st.core.design.molecules.buttons.ButtonState
import br.com.mob1st.core.design.molecules.texts.Header
import br.com.mob1st.core.design.organisms.ModalState
import br.com.mob1st.core.design.organisms.snack.SnackbarState
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
    fun snack(): SnackbarState

    /**
     * The default helper state for this error
     */
    fun modal(isDismissible: Boolean): ModalState

    /**
     * Represents an unknown error
     */
    data object Unknown : CommonError {
        override fun snack(): SnackbarState =
            SnackbarState(
                supporting = Text(R.string.utils_commonerror_snackbar_unknown_supporting),
                action = null,
            )

        override fun modal(isDismissible: Boolean): ModalState =
            ModalState(
                header =
                    Header(
                        text = Text(R.string.utils_commonerror_helper_noconnectivity_title),
                    ),
                message = Text(R.string.utils_commonerror_helper_noconnectivity_subtitle),
                buttonBar =
                    ButtonBar.SingleButton(
                        primary =
                            ButtonState(
                                text = Text(R.string.utils_commonerror_helper_unknown_button),
                            ),
                    ),
                isDismissible = isDismissible,
            )
    }

    /**
     * Represents a no connectivity error
     */
    data object NoConnectivity : CommonError {
        override fun snack(): SnackbarState =
            SnackbarState(
                supporting = Text(R.string.utils_commonerror_snackbar_noconnectivity_supporting),
                action = Text(R.string.utils_commonerror_snackbar_noconnectivity_action),
            )

        override fun modal(isDismissible: Boolean): ModalState =
            ModalState(
                header =
                    Header(
                        text = Text(R.string.utils_commonerror_helper_noconnectivity_title),
                    ),
                message = Text(R.string.utils_commonerror_helper_noconnectivity_subtitle),
                buttonBar =
                    ButtonBar.SingleButton(
                        primary =
                            ButtonState(
                                text = Text(R.string.utils_commonerror_helper_unknown_button),
                            ),
                    ),
                isDismissible = isDismissible,
            )
    }

    companion object {
        /**
         * Converts a [Throwable] into a [CommonError]
         */
        fun of(throwable: Throwable): CommonError {
            return when (throwable) {
                is NoConnectivityException -> NoConnectivity
                else -> Unknown
            }
        }
    }
}
