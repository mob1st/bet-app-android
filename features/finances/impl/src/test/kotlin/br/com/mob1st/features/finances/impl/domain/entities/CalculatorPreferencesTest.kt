package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.Test

class CalculatorPreferencesTest {
    @ParameterizedTest
    @MethodSource("appendSourceWithCentsDisabled")
    fun `GIVEN a edit cents disabled WHEN append number THEN assert it is multiplied by 100 And number times 100 is added`(
        initialAmount: Long,
        numberToAppend: Int,
        expectedResult: Long,
    ) {
        // Given
        val preferences = CalculatorPreferences(false)
        val amount = Money(initialAmount)
        val expected = Money(expectedResult)
        // When
        val actual = preferences.append(amount, numberToAppend)
        // Then
        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("appendSourceWithCentsEnabled")
    fun `GIVEN a edit cents enabled WHEN append number THEN assert it is multiplied by 1 And number times 1 is added`(
        initialAmount: Long,
        numberToAppend: Int,
        expectedResult: Long,
    ) {
        // Given
        val preferences = CalculatorPreferences(true)
        val amount = Money(initialAmount)
        val expected = Money(expectedResult)
        // When
        val actual = preferences.append(amount, numberToAppend)
        // Then
        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("eraseWithCentsEnabledSource")
    fun `GIVEN a edit cents enabled WHEN erase THEN assert it is divided by 10`(
        initialAmount: Long,
        expectedResult: Long,
    ) {
        // Given
        val preferences = CalculatorPreferences(true)
        val amount = Money(initialAmount)
        val expected = Money(expectedResult)
        // When
        val actual = preferences.erase(amount)
        // Then
        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("eraseWithCentsDisabledSource")
    fun `GIVEN a edit cents disabled WHEN erase THEN assert it is divided by 100`(
        initialAmount: Long,
        expectedResult: Long,
    ) {
        // Given
        val preferences = CalculatorPreferences(false)
        val amount = Money(initialAmount)
        val expected = Money(expectedResult)
        // When
        val actual = preferences.erase(amount)
        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a edit cents enabled WHEN toggle amount THEN assert amount is divided by 100`() {
        // Given
        val preferences = CalculatorPreferences(true)
        val amount = Money(100)
        val expected = Money(1)
        // When
        val actual = preferences.toggleAmount(amount)
        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a edit cents disabled WHEN toggle amount THEN assert amount is multiplied by 100`() {
        // Given
        val preferences = CalculatorPreferences(false)
        val amount = Money(1)
        val expected = Money(100)
        // When
        val actual = preferences.toggleAmount(amount)
        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a edit cents enabled WHEN toggle THEN assert it is toggled`() {
        // Given
        val preferences = CalculatorPreferences(true)
        val expected = CalculatorPreferences(false)
        // When
        val actual = preferences.toggleEditCents()
        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a edit cents disabled WHEN toggle THEN assert it is toggled`() {
        // Given
        val preferences = CalculatorPreferences(false)
        val expected = CalculatorPreferences(true)
        // When
        val actual = preferences.toggleEditCents()
        // Then
        assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        fun appendSourceWithCentsDisabled() = listOf(
            // initial state, number to append, expected state
            Arguments.arguments(0L, 0, 0L),
            Arguments.arguments(0L, 1, 100),
            Arguments.arguments(100L, 1, 1100L),
        )

        @JvmStatic
        fun appendSourceWithCentsEnabled() = listOf(
            // initial state, number to append, expected state
            Arguments.arguments(0L, 0, 0L),
            Arguments.arguments(0L, 1, 1L),
            Arguments.arguments(1L, 1, 11L),
            Arguments.arguments(100L, 1, 1001L),
        )

        @JvmStatic
        fun eraseWithCentsEnabledSource() = listOf(
            // initial state, expected state
            Arguments.arguments(0L, 0L),
            Arguments.arguments(900L, 90L),
            Arguments.arguments(99L, 9L),
            Arguments.arguments(9L, 0L),
        )

        @JvmStatic
        fun eraseWithCentsDisabledSource() = listOf(
            // initial state, expected state
            Arguments.arguments(0L, 0L),
            Arguments.arguments(100L, 0L),
            Arguments.arguments(900L, 0L),
            Arguments.arguments(990L, 0L),
            Arguments.arguments(9900L, 900L),
            Arguments.arguments(99900L, 9900L),
        )
    }
}
