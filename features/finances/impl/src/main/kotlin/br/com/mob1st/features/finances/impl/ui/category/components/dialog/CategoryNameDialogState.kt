package br.com.mob1st.features.finances.impl.ui.category.components.dialog

import androidx.compose.runtime.Immutable
import arrow.optics.optics

/**
 * The state of the category name dialog.
 * @property name The name of the category.
 */
@Immutable
@optics
data class CategoryNameDialogState(
    val name: String = "",
) {
    /**
     * Indicates if the submit button should be enabled.
     * Very short names are or very long names are not allowed.
     */
    val isSubmitEnabled: Boolean = name.isNotBlank() && name.length >= MIN_NAME_LENGTH

    companion object {
        private const val MIN_NAME_LENGTH = 3
    }
}
