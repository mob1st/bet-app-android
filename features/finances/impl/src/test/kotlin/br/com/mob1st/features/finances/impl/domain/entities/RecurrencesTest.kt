package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.features.finances.impl.domain.fixtures.fixedRecurrences
import br.com.mob1st.features.finances.impl.domain.fixtures.seasonalRecurrences
import br.com.mob1st.features.finances.impl.domain.fixtures.variableRecurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfYear
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.Test
import kotlin.test.assertEquals

class RecurrencesTest {
    @ParameterizedTest
    @MethodSource("recurrencesPerTypeSource")
    fun `GIVEN a recurrence WHEN get type THEN assert return is expected`(
        recurrences: Recurrences,
        expectedType: RecurrenceType,
    ) {
        val actual = recurrences.asType()
        assertEquals(expectedType, actual)
    }

    @ParameterizedTest
    @MethodSource("typesPerDefaultRecurrencesSource")
    fun `GIVEN a type WHEN get default recurrence THEN assert return is expected`(
        type: RecurrenceType,
        expectedRecurrences: Recurrences,
    ) {
        val actual = type.toDefaultRecurrences()
        assertEquals(expectedRecurrences, actual)
    }

    @ParameterizedTest
    @MethodSource("indexesPerDayOfMonthSource")
    fun `GIVEN an index WHEN get the selected day of month THEN assert the day is expected`(
        index: Int,
        expectedDay: Int,
    ) {
        val expected = Recurrences.Fixed(DayOfMonth(expectedDay))
        val actual = Recurrences.Fixed.selectDay(index)
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a list of indexes WHEN create seasonal recurrence from selected months THEN assert the days of year are expected`() {
        val indexes = listOf(0, 1, 2)
        val expected = Recurrences.Seasonal(
            daysOfYear = listOf(
                DayOfYear(1),
                DayOfYear(32),
                DayOfYear(60),
            ),
        )
        val actual = Recurrences.Seasonal.fromSelectedMonths(indexes)
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN days of year WHEN get selected months THEN assert months are expected`() {
        val daysOfYear = listOf(
            DayOfYear(1),
            DayOfYear(32),
            DayOfYear(365),
        )
        val expected = listOf(0, 1, 11)
        val actual = Recurrences.Seasonal(daysOfYear).selectMonthsIndexes()
        assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        fun recurrencesPerTypeSource() = listOf(
            arguments(
                Arb.fixedRecurrences().next(),
                RecurrenceType.Fixed,
            ),
            arguments(
                Arb.variableRecurrences().next(),
                RecurrenceType.Variable,
            ),
            arguments(
                Arb.seasonalRecurrences().next(),
                RecurrenceType.Seasonal,
            ),
        )

        @JvmStatic
        fun typesPerDefaultRecurrencesSource() = listOf(
            arguments(
                RecurrenceType.Fixed,
                Recurrences.Fixed(DayOfMonth(1)),
            ),
            arguments(
                RecurrenceType.Variable,
                Recurrences.Variable,
            ),
            arguments(
                RecurrenceType.Seasonal,
                Recurrences.Seasonal(
                    daysOfYear = emptyList(),
                ),
            ),
        )

        @JvmStatic
        fun indexesPerDayOfMonthSource() = listOf(
            arguments(0, 1),
            arguments(1, 2),
            arguments(30, 31),
        )
    }
}
