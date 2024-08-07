package br.com.mob1st.core.design.molecules.keyboard

import androidx.compose.runtime.Immutable

/**
 * A key that can be clicked in the [Keyboard] component.
 */
sealed interface Key

/**
 * A numeric key that can be clicked in the [Keyboard] component.
 * It can be any number between 0 and 9.
 * The constructor is private to avoid creating instances with invalid numbers.
 * Use the [NumericKey.Instances] object to get the available instances.
 */
@Immutable
@JvmInline
value class NumericKey private constructor(
    val number: Int,
) : Key {
    init {
        require(number in 0..9) { "Number must be between 0 and 9. Current is $number" }
    }

    /**
     * The available instances of the [NumericKey].
     */
    companion object Instances {
        val Zero = NumericKey(0)
        val One = NumericKey(1)
        val Two = NumericKey(2)
        val Three = NumericKey(3)
        val Four = NumericKey(4)
        val Five = NumericKey(5)
        val Six = NumericKey(6)
        val Seven = NumericKey(7)
        val Eight = NumericKey(8)
        val Nine = NumericKey(9)
    }
}

/**
 * Keys that, differently from the [NumericKey], have a specific behavior when clicked.
 */
sealed interface FunctionKey : Key {
    /**
     * When clicked, it indicates the amount type should be undone and the original value should be restored.
     */
    data object Undo : FunctionKey

    /**
     * Removes the last character from the amount.
     */
    data object Delete : FunctionKey

    /**
     * Submits the information on screen and dismisses the keyboard.
     */
    data object Done : FunctionKey

    /**
     * Allows the user to select a date.
     */
    data object Calendar : FunctionKey

    /**
     * Allows the user to set cents for the amount.
     */
    data object Decimal : FunctionKey
}
