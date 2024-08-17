package br.com.mob1st.features.finances.impl.ui.category.detail

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.LocalizedText
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.scaleDown
import br.com.mob1st.core.kotlinx.structures.scaleUp
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategoryDetail
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfYear
import br.com.mob1st.features.finances.impl.domain.values.fromMonth
import br.com.mob1st.features.finances.impl.ui.utils.texts.MoneyLocalizedText
import kotlinx.datetime.Month

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
            return entry.copy(amount = entry.amount.scaleUp(number, detail.preferences.isCentsEnabled))
        }

        /**
         * Erase the minor unit of the amount.
         * It has the opposite effect of [appendNumber].
         * @return A new [CategoryEntry] with the erased amount.
         */
        fun erase(): CategoryEntry {
            return entry.copy(amount = entry.amount.scaleDown(detail.isCentsEnabled))
        }

        /**
         * Toggles the decimal mode of the amount.
         */
        fun toggleDecimalMode(): Loaded {
            val newSelection = !detail.isCentsEnabled
            val newAmount = if (newSelection) {
                entry.amount / Money.CENT_SCALE
            } else {
                entry.amount * Money.CENT_SCALE
            }
            return copy(
                entry = CategoryEntry.amount.set(entry, newAmount),
                detail = detail.setIsCentsEnabled(newSelection),
            )
        }

        /**
         * Submits the dialog to the [CategoryEntry] returning a new [CategoryEntry] with the updated values.
         */
        fun submitDialog(dialog: CategoryDetailConsumables.Dialog): CategoryEntry {
            return when (dialog) {
                is IconPickerDialog -> entry.copy(image = dialog.selected)
                is EditCategoryNameDialog -> entry.copy(name = dialog.name)
                is EditRecurrencesDialog.Fixed -> entry.copy(
                    recurrences = Recurrences.Fixed(
                        day = DayOfMonth.allDays[dialog.selected],
                    ),
                )

                is EditRecurrencesDialog.Seasonal -> entry.copy(
                    recurrences = Recurrences.Seasonal(
                        daysOfYear = dialog.selected.map { index ->
                            DayOfYear.fromMonth(Month.entries[index])
                        },
                    ),
                )

                VariableNotAllowEditionDialog -> entry
            }
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
