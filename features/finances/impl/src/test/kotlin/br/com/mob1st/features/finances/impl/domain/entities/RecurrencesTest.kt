package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.fixedRecurrences
import br.com.mob1st.features.finances.impl.domain.values.seasonalRecurrences
import br.com.mob1st.features.finances.impl.domain.values.variableRecurrences
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
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
    }
}
