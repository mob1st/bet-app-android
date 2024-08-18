package br.com.mob1st.core.kotlinx.structures

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.provider.Arguments.arguments

class MoneyTest {
    @Test
    fun `GIVEN an amount WHEN sum THEN should return the sum of the amounts`() {
        val expected = Money(100)
        val actual = Money(50) + Money(50)
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN an amount WHEN subtract THEN should return the subtraction of the amounts`() {
        val expected = Money(50)
        val actual = Money(100) - Money(50)
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a greater amount WHEN compareTo THEN should return a positive number`() {
        val expected = 1
        val actual = Money(100).compareTo(Money(50))
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a smaller amount WHEN compareTo THEN should return a negative number`() {
        val expected = -1
        val actual = Money(50).compareTo(Money(100))
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a equal amount WHEN compareTo THEN should return zero`() {
        val expected = 0
        val actual = Money(50).compareTo(Money(50))
        assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        fun scaleUpSource() = listOf(
            arguments(
                // initial state
                0L,
                // is cents enabled
                false,
                // new amount
                1,
                // expected state
                100L,
            ),
            arguments(
                // initial state
                100L,
                // is cents enabled
                false,
                // new amount
                1,
                // expected state
                1100L,
            ),
            arguments(
                // initial state
                100L,
                // is cents enabled
                true,
                // new amount
                1,
                // expected state
                1001L,
            ),
            arguments(
                // initial state
                0L,
                // is cents enabled
                true,
                // new amount
                1,
                // expected state
                1L,
            ),
            arguments(
                // initial state
                1L,
                // is cents enabled
                true,
                // new amount
                1,
                // expected state
                11L,
            ),
        )

        @JvmStatic
        fun eraseSource() = listOf(
            arguments(
                // initial state
                0L,
                true,
                // expected state
                0L,
            ),
            arguments(
                900L,
                true,
                90L,
            ),
            arguments(
                99L,
                true,
                9L,
            ),
            arguments(
                // initial state
                9L,
                true,
                // expected state
                0L,
            ),
            arguments(
                // initial state
                0L,
                false,
                // expected state
                0L,
            ),
            arguments(
                900L,
                false,
                0L,
            ),
            arguments(
                9900L,
                false,
                900L,
            ),
            arguments(
                99900L,
                false,
                9900L,
            ),
            arguments(
                1000L,
                false,
                100L,
            ),
            arguments(
                100L,
                false,
                0L,
            ),
        )
    }
}
