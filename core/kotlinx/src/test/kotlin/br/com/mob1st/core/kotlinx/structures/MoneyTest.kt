package br.com.mob1st.core.kotlinx.structures

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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
}
