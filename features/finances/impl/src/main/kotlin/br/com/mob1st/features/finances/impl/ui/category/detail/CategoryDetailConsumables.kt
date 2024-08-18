package br.com.mob1st.features.finances.impl.ui.category.detail

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import br.com.mob1st.core.design.atoms.properties.texts.LocalizedText
import br.com.mob1st.core.design.organisms.snack.SnackbarState
import br.com.mob1st.core.kotlinx.checks.checkIs
import br.com.mob1st.core.kotlinx.structures.IndexSelectableState
import br.com.mob1st.core.kotlinx.structures.MultiIndexSelectableState
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.entities.selectMonthsIndexes
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.ui.category.components.dialog.CategoryNameDialogState
import br.com.mob1st.features.finances.impl.ui.category.components.dialog.name
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Month

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
    fun showRecurrencesDialog(recurrences: Recurrences): CategoryDetailConsumables {
        val dialog = when (recurrences) {
            is Recurrences.Fixed -> EditRecurrencesDialog.Fixed(
                selected = DayOfMonth.allDays.indexOf(recurrences.day),
            )

            is Recurrences.Seasonal -> EditRecurrencesDialog.Seasonal(
                selected = recurrences.selectMonthsIndexes().toImmutableList(),
            )

            Recurrences.Variable -> VariableNotAllowEditionDialog
        }
        return copy(dialog = dialog)
    }

    /**
     * Shows the undo dialog to ensure that the user wants to undo the changes made.
     * @return A new [CategoryDetailConsumables] with the dialog set.
     */
    fun showIconPickerDialog(selected: Uri): CategoryDetailConsumables {
        return copy(
            dialog = IconPickerDialog(selected),
        )
    }

    /**
     * Opens a dialog to allow the user to type a new name for the category.
     * @param initialName The initial name to be shown in the dialog.
     * @return A new [CategoryDetailConsumables] with the dialog set.
     */
    fun showEnterCategoryNameDialog(initialName: String): CategoryDetailConsumables {
        return copy(dialog = EditCategoryNameDialog(initialName))
    }

    /**
     * Sets the name of the category in the dialog.
     * @param name The new name to be set.
     * @return A new [CategoryDetailConsumables] with the dialog set.
     * @throws IllegalStateException If the dialog is not a [EditCategoryNameDialog].
     */
    fun setName(
        name: String,
    ): CategoryDetailConsumables {
        val dialog = checkIs<EditCategoryNameDialog>(dialog)
        return CategoryDetailConsumables.dialog.set(
            this,
            EditCategoryNameDialog.state.name.set(dialog, name),
        )
    }

    /**
     * For [optics] generation.
     */
    companion object
}

/**
 * Allows the user to type a new name for the category
 * @property state The current state of the dialog.
 */
@optics
@Immutable
data class EditCategoryNameDialog(
    val state: CategoryNameDialogState = CategoryNameDialogState(),
) : CategoryDetailConsumables.Dialog {
    val name = state.name

    constructor(name: String) : this(CategoryNameDialogState(name))

    /**
     * for [optics] generation
     */
    companion object
}

/**
 * Allows the user to set a new recurrence date for the category.
 * @property recurrences The recurrences that can be set as the new category recurrences.
 */
@Immutable
sealed interface EditRecurrencesDialog : CategoryDetailConsumables.Dialog {
    @Immutable
    data class Fixed(
        override val selected: Int,
    ) : EditRecurrencesDialog, IndexSelectableState<LocalizedText> {
        override val options = DayOfMonth.allDays.map {
            LocalizedText(it.toString())
        }.toImmutableList()
    }

    @Immutable
    data class Seasonal(
        override val selected: ImmutableList<Int>,
    ) : EditRecurrencesDialog, MultiIndexSelectableState<LocalizedText> {
        override val options = Month.entries.map {
            // TODO write MonthLocalizedText
            LocalizedText(it.name.lowercase())
        }.toImmutableList()
    }
}

/**
 * The dialog to show the user that the variable recurrences cannot be edited.
 */
data object VariableNotAllowEditionDialog : CategoryDetailConsumables.Dialog

@Immutable
internal data class IconPickerDialog(
    val selected: Uri,
    val query: String = "",
    val listOfIcons: ImmutableList<Uri> = persistentListOf(),
) : CategoryDetailConsumables.Dialog
