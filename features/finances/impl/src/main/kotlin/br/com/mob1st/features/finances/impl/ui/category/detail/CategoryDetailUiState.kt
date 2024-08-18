package br.com.mob1st.features.finances.impl.ui.category.detail

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.LocalizedText
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategoryDetail
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.ui.utils.texts.MoneyLocalizedText

/**
 * Ui state for the category detail screen.
 * All implementations must be annotated with [Immutable].
 */
@Immutable
internal sealed interface CategoryDetailUiState {
    /**
     * Represents the loading state of the screen. It's the initial state.
     */
    @Immutable
    data object Loading : CategoryDetailUiState

    /**
     * Represents the loaded state of the screen.
     * @property category The category to be shown.
     * @property entry The data edited by the user.
     */
    @Immutable
    data class Loaded(
        val detail: CategoryDetail,
        val entry: CategoryEntry,
    ) : CategoryDetailUiState {
        /**
         * Returns a [LocalizedText] representation of the amount with the currency symbol.
         */
        val amount: LocalizedText = MoneyLocalizedText(entry.amount)

        /**
         * The name of the category.
         */
        val name: String = entry.name

        /**
         * Append the given [number] as the minor unit of the amount.
         * @return A new [CategoryEntry] with the new amount.
         */
        fun appendNumber(number: Int): CategoryEntry {
            return entry.copy(amount = detail.preferences.append(entry.amount, number))
        }

        /**
         * Erase the minor unit of the amount.
         * It has the opposite effect of [appendNumber].
         * @return A new [CategoryEntry] with the erased amount.
         */
        fun erase(): CategoryEntry {
            return entry.copy(amount = detail.preferences.erase(entry.amount))
        }

        /**
         * Toggles the decimal mode of the amount.
         */
        fun toggleDecimalMode(): Loaded {
            val toggledPreferences = detail.preferences.toggleEditCents()
            val newAmount = toggledPreferences.toggleAmount(entry.amount)
            return copy(
                detail = detail.copy(preferences = toggledPreferences),
                entry = entry.copy(amount = newAmount),
            )
        }

        /**
         * Submits the dialog to the [CategoryEntry] returning a new [CategoryEntry] with the updated values.
         */
        fun submitDialog(dialog: CategoryDetailConsumables.Dialog): CategoryEntry = when (dialog) {
            is IconPickerDialog -> entry.copy(image = dialog.selected)
            is EditCategoryNameDialog -> entry.copy(name = dialog.name)
            is EditRecurrencesDialog.Fixed -> entry.copy(
                recurrences = Recurrences.Fixed.selectDay(dialog.selected),
            )

            is EditRecurrencesDialog.Seasonal -> entry.copy(
                recurrences = Recurrences.Seasonal.selectMonths(dialog.selected),
            )

            VariableNotAllowEditionDialog -> entry
        }

        /**
         * Merge the [entry] into the [category] returning a new [Category] with the updated values.
         * @return A new [Category] with the updated values.
         */
        fun merge(): Category {
            return detail.category.copy(
                amount = entry.amount,
                name = entry.name,
                recurrences = entry.recurrences,
                image = entry.image,
            )
        }
    }
}
