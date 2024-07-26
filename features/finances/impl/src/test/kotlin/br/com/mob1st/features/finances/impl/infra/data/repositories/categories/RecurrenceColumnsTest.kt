package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.fixtures.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.fixtures.DayOfYear
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertFailsWith

internal class RecurrenceColumnsTest {
    @ParameterizedTest
    @ArgumentsSource(RecurrenceProvider::class)
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
    @ArgumentsSource(RawRecurrenceProvider::class)
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

    object RecurrenceProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    Recurrences.Variable,
                    RecurrenceColumns("variable", null),
                ),
                Arguments.of(
                    Recurrences.Fixed(DayOfMonth(1)),
                    RecurrenceColumns("fixed", "01"),
                ),
                Arguments.of(
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
        }
    }

    object RawRecurrenceProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    "variable",
                    null,
                    Recurrences.Variable,
                ),
                Arguments.of(
                    "fixed",
                    "01",
                    Recurrences.Fixed(DayOfMonth(1)),
                ),
                Arguments.of(
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
        }
    }
}
