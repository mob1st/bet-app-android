package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.fixtures.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.fixtures.DayOfYear
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrenceType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertFailsWith

internal class RecurrenceColumnsTest {
    @ParameterizedTest
    @MethodSource("recurrencesSource")
    fun `WHEN create from recurrence THEN assert return is expected`(
        recurrence: Recurrences,
        expected: RecurrenceColumns,
    ) {
        // When
        val recurrenceColumns = RecurrenceColumns.from(recurrence)
        // Then
        assertEquals(expected, recurrenceColumns)
    }

    @Test
    fun `GIVEN a seasonal recurrence with one day WHEN create from THEN assert return is expected`() {
        // Given
        val recurrence = Recurrences.Seasonal(listOf(DayOfYear(1)))
        val expected = RecurrenceColumns("seasonal", "001")
        // When
        val recurrenceColumns = RecurrenceColumns.from(recurrence)
        // Then
        assertEquals(expected, recurrenceColumns)
    }

    @Test
    fun `GIVEN a seasonal recurrence with no days WHEN create from THEN assert return is expected`() {
        // Given
        val recurrence = Recurrences.Seasonal(emptyList())
        val expected = RecurrenceColumns("seasonal", null)
        // When
        val recurrenceColumns = RecurrenceColumns.from(recurrence)
        // Then
        assertEquals(expected, recurrenceColumns)
    }

    @ParameterizedTest
    @MethodSource("rawTypeToRecurrencesSource")
    fun `GIVEN a raw recurrence type AND a raw recurrence WHEN get domain recurrence THEN assert result`(
        rawType: String,
        rawRecurrences: String?,
        expected: Recurrences,
    ) {
        // Given
        val recurrenceColumns = RecurrenceColumns(rawType, rawRecurrences)
        // When
        val actual = recurrenceColumns.toRecurrences()
        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a raw seasonal recurrence And one day WHEN get domain recurrence THEN assert result`() {
        // GIVEN
        val recurrenceColumns = RecurrenceColumns("seasonal", "001")
        // WHEN
        val actual = recurrenceColumns.toRecurrences()
        // THEN
        val expected = Recurrences.Seasonal(listOf(DayOfYear(1)))
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a raw seasonal recurrence And null recurrence WHEN get domain recurrence THEN assert result`() {
        // GIVEN
        val recurrenceColumns = RecurrenceColumns("seasonal", null)
        // WHEN
        val actual = recurrenceColumns.toRecurrences()
        // THEN
        val expected = Recurrences.Seasonal(emptyList())
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a raw fixed recurrence And null recurrence WHEN get domain recurrence THEN assert failure`() {
        val recurrenceColumns = RecurrenceColumns("fixed", null)
        assertFailsWith<IllegalStateException> {
            recurrenceColumns.toRecurrences()
        }
    }

    @ParameterizedTest
    @MethodSource("enumToRawType")
    fun `GIVEN a recurrence type WHEN map to raw type THEN assert result`(
        recurrenceType: RecurrenceType,
        expected: String,
    ) {
        val actual = RecurrenceColumns.rawTypeFrom(recurrenceType)
        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("rawTypeToEnum")
    fun `GIVEN a raw type WHEN map to recurrence type THEN assert result`(
        rawType: String,
        expected: RecurrenceType,
    ) {
        val actual = RecurrenceColumns(rawType, "").rawTypeTo()
        assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        fun recurrencesSource() = listOf(
            arguments(
                Recurrences.Variable,
                RecurrenceColumns("variable", null),
            ),
            arguments(
                Recurrences.Fixed(DayOfMonth(1)),
                RecurrenceColumns("fixed", "01"),
            ),
            arguments(
                Recurrences.Seasonal(
                    listOf(
                        DayOfYear(1),
                        DayOfYear(30),
                        DayOfYear(200),
                    ),
                ),
                RecurrenceColumns("seasonal", "001,030,200"),
            ),
        )

        @JvmStatic
        fun rawTypeToRecurrencesSource() = listOf(
            arguments(
                "variable",
                null,
                Recurrences.Variable,
            ),
            arguments(
                "fixed",
                "01",
                Recurrences.Fixed(DayOfMonth(1)),
            ),
            arguments(
                "seasonal",
                "001,030,200",
                Recurrences.Seasonal(
                    listOf(
                        DayOfYear(1),
                        DayOfYear(30),
                        DayOfYear(200),
                    ),
                ),
            ),
        )

        @JvmStatic
        fun enumToRawType() = listOf(
            arguments(
                RecurrenceType.Fixed,
                "fixed",
            ),
            arguments(
                RecurrenceType.Variable,
                "variable",
            ),
            arguments(
                RecurrenceType.Seasonal,
                "seasonal",
            ),
        )

        @JvmStatic
        fun rawTypeToEnum() = listOf(
            arguments(
                "fixed",
                RecurrenceType.Fixed,
            ),
            arguments(
                "variable",
                RecurrenceType.Variable,
            ),
            arguments(
                "seasonal",
                RecurrenceType.Seasonal,
            ),
        )
    }
}
