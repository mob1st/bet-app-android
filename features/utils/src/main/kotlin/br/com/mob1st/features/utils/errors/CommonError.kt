package br.com.mob1st.features.utils.errors

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.Text
import br.com.mob1st.core.design.molecules.buttons.ButtonState
import br.com.mob1st.core.design.organisms.helper.HelperState
import br.com.mob1st.core.design.organisms.snack.SnackState
import br.com.mob1st.core.kotlinx.errors.NoConnectivityException
import br.com.mob1st.features.utils.errors.CommonError.NoConnectivity
import br.com.mob1st.features.utils.errors.CommonError.Unknown

/**
 * Represents a message to be displayed to the user through a [SnackState].
 * It's a sealed class with 3 implementations:
 * - [Unknown] for unknown errors
 * - [NoConnectivity] for no connectivity errors
 */
@Immutable
sealed interface CommonError {

    val snack: SnackState
    val helper: HelperState

    /**
     * Represents an unknown error
     */
    data object Unknown : CommonError {
        override val snack: SnackState = SnackState(
            supporting = Text("Unknown error"),
            action = Text("Retry")
        )
        override val helper: HelperState = HelperState(
            title = Text("Unknown error"),
            subtitle = Text("Something went wrong"),
            buttonState = ButtonState(
                text = Text("Retry")
            )
        )
    }

    /**
     * Represents a no connectivity error
     */
    data object NoConnectivity : CommonError {
        override val snack: SnackState = SnackState(
            supporting = Text("No connectivity"),
            action = Text("Settings")
        )

        override val helper: HelperState = HelperState(
            title = Text("No connectivity"),
            subtitle = Text("Please check your internet connection"),
            buttonState = ButtonState(
                text = Text("Settings")
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
