package br.com.mob1st.core.kotlinx.structures

/**
 * Immutable value object to be used as money in cents.
 * @property cents The amount of cents.
 */
@JvmInline
value class Money(
    val cents: Long,
) : Comparable<Money> {
    /**
     * Adds two [Money] values generating a new one.
     */
    operator fun plus(other: Money) = Money(cents + other.cents)

    operator fun plus(other: Int) = Money(cents + other)

    /**
     * Subtracts two [Money] values generating a new one.
     */
    operator fun minus(other: Money) = Money(cents - other.cents)

    /**
     * Multiplies the [Money] value by a scalar.
     */
    operator fun times(other: Int) = Money((cents * other))

    /**
     * Divides the [Money] value by a scalar.
     */
    operator fun div(other: Int) = Money((cents / other))

    /**
     * Compares two [Money] values.
     */
    override fun compareTo(other: Money): Int {
        return cents.compareTo(other.cents)
    }

    companion object {
        const val CENT_SCALE = 100
        const val DECIMAL_SCALE = 10
        private const val CURRENCY_REGEX = "[^\\d-]"

        val Zero = Money(0)

        /**
         * @throws NumberFormatException if the string is not a valid number.
         */
        fun from(
            string: String,
        ): Money {
            val onlyNumericCharacters = string.replace(Regex(CURRENCY_REGEX), "")
            return Money(onlyNumericCharacters.toLong())
        }
    }
}
