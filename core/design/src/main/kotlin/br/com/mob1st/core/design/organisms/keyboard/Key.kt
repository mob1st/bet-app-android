package br.com.mob1st.core.design.organisms.keyboard

import androidx.compose.runtime.Immutable

sealed interface Key

@Immutable
@JvmInline
value class NumericKey private constructor(
    val number: Int,
) : Key {
    init {
        require(number in 0..9) { "Number must be between 0 and 9. Current is $number" }
    }

    companion object {
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

sealed interface FunctionKey : Key {
    data object Undo : FunctionKey

    data object Delete : FunctionKey

    data object Done : FunctionKey

    data object Calendar : FunctionKey

    data object Decimal : FunctionKey
}
