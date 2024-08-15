package br.com.mob1st.features.finances.impl.ui.category.detail

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import br.com.mob1st.core.design.organisms.snack.SnackbarState
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences

/**
 * Consumables for the category detail screen.
 * @property dialog The dialog to be shown.
 * @property snackbarState The snackbar to be shown.
 * @property isSubmitted Indicates if the form was submitted or not.
 */
@Immutable
@optics
data class CategoryDetailConsumables(
    val dialog: Dialog? = null,
    val snackbarState: SnackbarState? = null,
    val isSubmitted: Boolean = false,
) {
    /**
     * The possible dialogs that can be consumed in the category detail screen.
     */
    sealed interface Dialog

    /**
     * Show the proper [Dialog] for the given [recurrences].
     * @param recurrences The recurrences to be shown.
     * @return A new [CategoryDetailConsumables] with the dialog set.
     */
    fun showDialog(recurrences: Recurrences): CategoryDetailConsumables {
        return copy(dialog = CategoryCalendarDialog(recurrences))
    }

    /**
     * For [optics] generation.
     */
    companion object
}

/**
 * Allows the user to type a new name for the category
 * @property name The name typed by the user that can be set as the new category name.
 */
@Immutable
data class CategoryNameDialog(
    val name: String,
) : CategoryDetailConsumables.Dialog

/**
 * Allows the user to set a new recurrence date for the category.
 * @property recurrences The recurrences that can be set as the new category recurrences.
 */
@Immutable
data class CategoryCalendarDialog(
    val recurrences: Recurrences,
) : CategoryDetailConsumables.Dialog
