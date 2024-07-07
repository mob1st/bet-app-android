package br.com.mob1st.core.design.organisms.snack

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/**
 * Provides the visuals for a snackbar.
 */
@Immutable
interface SnackbarState {
    /**
     * Resolves the visuals for the snackbar.
     * @return The visuals for the snackbar.
     */
    @Composable
    fun resolve(): SnackbarVisuals
}

/**
 * Helper function to create a implementation of [SnackbarVisuals].
 * @param message The message to be shown.
 * @param actionLabel The label for the action button.
 * @param duration The duration of the snackbar.
 * @param withDismissAction Indicates if the snackbar should have a dismiss action.
 * @return The snackbar visuals.
 */
fun snackbar(
    message: String,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
    withDismissAction: Boolean = false,
): SnackbarVisuals = SnackbarVisualsImpl(
    message = message,
    actionLabel = actionLabel,
    duration = duration,
    withDismissAction = withDismissAction,
)

@Immutable
private data class SnackbarVisualsImpl(
    override val message: String,
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val withDismissAction: Boolean = false,
) : SnackbarVisuals
