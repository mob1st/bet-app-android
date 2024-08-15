package br.com.mob1st.features.finances.impl.ui.category.detail

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.LocalizedText
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.domain.entities.Category
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
        val category: Category,
        val entry: CategoryEntry,
    ) : CategoryDetailUiState {
        /**
         * Returns a [LocalizedText] representation of the amount with the currency symbol.
         */
        val amount: LocalizedText = MoneyLocalizedText(entry.amount)

        /**
         * Append the given [number] as the minor unit of the amount.
         * @return A new [CategoryEntry] with the new amount.
         */
        fun appendNumber(number: Int): CategoryEntry {
            val newValue = (entry.amount * DECIMAL_MULTIPLIER) + (number * Money.SCALE.toInt())
            return entry.copy(amount = newValue)
        }

        /**
         * Erase the minor unit of the amount.
         * It has the opposite effect of [appendNumber].
         * @return A new [CategoryEntry] with the erased amount.
         */
        fun erase(): CategoryEntry {
            val currentValue = entry.amount.cents / Money.SCALE.toInt()
            val newValue = currentValue / DECIMAL_MULTIPLIER
            return entry.copy(amount = Money(newValue * Money.SCALE.toInt()))
        }

        /**
         * Reverts all entries returning it to it's initial state using the [category] as the base.
         * @return A new [CategoryEntry] with the initial values.
         */
        fun undo(): CategoryEntry = CategoryEntry(category)

        /**
         * Merge the [entry] into the [category] returning a new [Category] with the updated values.
         * @return A new [Category] with the updated values.
         */
        fun merge(): Category {
            return category.copy(
                amount = entry.amount,
                name = entry.name,
                recurrences = entry.recurrences,
                image = entry.image,
            )
        }
    }

    companion object {
        private const val DECIMAL_MULTIPLIER = 10
    }
}
