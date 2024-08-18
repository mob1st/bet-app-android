package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money

data class CalculatorPreferences(
    val isEditCentsEnabled: Boolean,
) {
    fun toggleEditCents(): CalculatorPreferences = copy(isEditCentsEnabled = !isEditCentsEnabled)

    /**
     * Toggles the amount between cents and dollars. If the [isEditCentsEnabled] is true, it will remove two decimal places,
     * otherwise, it will add two decimal places to the given [amount].
     * @param amount The amount that will be used as reference to toggle.
     * @return The toggled amount.
     */
    fun toggleAmount(amount: Money): Money {
        return if (isEditCentsEnabled) {
            amount / Money.CENT_SCALE
        } else {
            amount * Money.CENT_SCALE
        }
    }

    /**
     * Appends a number to the current [Money] value.
     * @param amount The current amount.
     * @param number The number to be appended.
     * @return A new [Money] value with the number appended.
     */
    fun append(
        amount: Money,
        number: Int,
    ): Money {
        val (currentCentsScale, newNumberScale) = if (isEditCentsEnabled) {
            Money.DECIMAL_SCALE to 1
        } else {
            Money.DECIMAL_SCALE to Money.CENT_SCALE
        }
        return Money(amount.cents * currentCentsScale + number * newNumberScale)
    }

    /**
     * Erases the lowest decimal unit in the [Money] value.
     * @param amount The current amount.
     * @return A new [Money] value with the lowest decimal unit erased.
     */
    fun erase(amount: Money): Money {
        return if (isEditCentsEnabled) {
            Money(amount.cents / Money.DECIMAL_SCALE)
        } else {
            val scaledDown = amount.cents / Money.DECIMAL_SCALE
            val rounded = scaledDown / Money.CENT_SCALE * Money.CENT_SCALE
            Money(rounded)
        }
    }
}
