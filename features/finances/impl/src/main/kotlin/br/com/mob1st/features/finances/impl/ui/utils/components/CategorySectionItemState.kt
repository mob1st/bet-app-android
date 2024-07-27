package br.com.mob1st.features.finances.impl.ui.utils.components

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.colors.BackgroundContainerCombination
import br.com.mob1st.core.design.atoms.properties.colors.ContextualizedColorCombination
import br.com.mob1st.core.design.atoms.properties.colors.FixedContainerCombination
import br.com.mob1st.core.design.atoms.properties.colors.IncomesContainerCombination
import br.com.mob1st.core.design.atoms.properties.colors.SeasonalContainerCombination
import br.com.mob1st.core.design.atoms.properties.colors.VariableContainerCombination
import br.com.mob1st.core.design.atoms.properties.texts.LocalizedText
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.R
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.ui.utils.texts.FixedRecurrencesLocalizedText
import br.com.mob1st.features.finances.impl.ui.utils.texts.MoneyLocalizedText
import br.com.mob1st.features.finances.impl.ui.utils.texts.SeasonalRecurrencesLocalizedText

/**
 * Abstraction of a list item to present in the category builder screen.
 * It can be a suggestion, a manually added category, or the "Add category" item.
 */
@Immutable
data class CategorySectionItemState(
    val category: Category,
) {
    /**
     * Unique identifier for the item.
     * It can be used to optimize the list rendering.
     */
    val key: Long = category.id.value

    /**
     * The icon of the item.
     */
    val icon: Icon = Icon(
        combination = category.toIconBackground(),
        image = category.image,
    )

    /**
     * The leading text of the item. Usually it's the category name or the main instruction
     */
    val name: String = category.name

    /**
     * The supporting text of the item. It's usually the category description, if any.
     */
    val recurrences: LocalizedText? = category.recurrences.asText()

    /**
     * The value text of the item. It's usually the category amount, if any.
     */
    val amount: Amount = Amount(
        amount = MoneyLocalizedText(category.amount),
        supporting = category.recurrences.toSupportTrailing(),
    )

    /**
     * The icon of for the category.
     * @property combination The color family of the icon's background.
     * @property image The image of the icon.
     */
    @Immutable
    data class Icon(
        val combination: ContextualizedColorCombination,
        val image: Uri,
    )

    /**
     * The trailing text of the item.
     */
    @Immutable
    data class Amount(
        val amount: LocalizedText,
        val supporting: LocalizedText,
    )
}

/**
 * The text state for fixed recurrences.
 */
private fun Category.toIconBackground(): ContextualizedColorCombination {
    if (amount <= Money.Zero) {
        return BackgroundContainerCombination
    }
    return when (recurrences) {
        is Recurrences.Fixed -> {
            if (isExpense) {
                FixedContainerCombination
            } else {
                IncomesContainerCombination
            }
        }

        is Recurrences.Seasonal -> SeasonalContainerCombination
        is Recurrences.Variable -> VariableContainerCombination
    }
}

/**
 * The text state for fixed recurrences.
 */
private fun Recurrences.toSupportTrailing(): LocalizedText {
    val resId = when (this) {
        is Recurrences.Fixed -> R.string.finances_commons__category_item__trailing_support_fixed
        is Recurrences.Seasonal -> R.string.finances_commons__category_item__trailing_support_seasonal
        is Recurrences.Variable -> R.string.finances_commons__category_item__trailing_support_variable
    }
    return LocalizedText(resId)
}

private fun Recurrences.asText(): LocalizedText? {
    return when (this) {
        is Recurrences.Fixed -> FixedRecurrencesLocalizedText(this)
        is Recurrences.Seasonal -> if (daysOfYear.isNotEmpty()) {
            SeasonalRecurrencesLocalizedText(this)
        } else {
            null
        }

        is Recurrences.Variable -> null
    }
}
