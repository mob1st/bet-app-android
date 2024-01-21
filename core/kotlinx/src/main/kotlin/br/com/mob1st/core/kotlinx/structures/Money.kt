package br.com.mob1st.core.kotlinx.structures

/**
 * Immutable value object to be used as money in cents.
 * @property cents The amount of cents.
 */
@JvmInline
value class Money(val cents: Int) : Comparable<Money> {

    init {
        require(cents >= 0) { "Money cannot be negative" }
    }

    /**
     * Adds two [Money] values generating a new one.
     */
    operator fun plus(other: Money) = Money(cents + other.cents)

    /**
     * Subtracts two [Money] values generating a new one.
     */
    operator fun minus(other: Money) = Money(cents - other.cents)

    /**
     * Multiplies two [Money] values generating a new one.
     */
    operator fun times(other: Money) = Money(cents * other.cents)

    /**
     * Divides two [Money] values generating a new one.
     */
    operator fun div(other: Money) = Money(cents / other.cents)

    /**
     * Compares two [Money] values.
     */
    override fun compareTo(other: Money): Int {
        return cents.compareTo(other.cents)
    }

    companion object {
        val Zero = Money(0)
    }
}
