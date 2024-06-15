package br.com.mob1st.features.utils.errors

import androidx.compose.runtime.Immutable
import br.com.mob1st.features.utils.errors.CommonError.Unknown

/**
 * Common errors that are usually handled by all features.
 *
 * They are the type of errors that shows some kind of helper to the user and a predefined action, doesn't matter which
 * feature is being used.
 *
 * It's a sealed class with 2 implementations:
 * - [Unknown] for unknown errors
 */
@Immutable
sealed interface CommonError {
    /**
     * Represents an unknown error
     */
    data object Unknown : CommonError
}

/**
 * Converts a [Throwable] to a [CommonError].
 * If the receiver throwable is not recognized, it returns [Unknown].
 * @return a [CommonError] that represents the throwable
 */
fun Throwable.toCommonError(): CommonError {
    return Unknown
}
