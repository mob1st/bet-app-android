package br.com.mob1st.features.finances.impl.ui.utils.texts

import br.com.mob1st.core.design.atoms.properties.texts.LocalizedText
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfYear
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class RecurrencesLocalizedTextFactoryTest {
    @ParameterizedTest
    @MethodSource("recurrencesSource")
    fun `GIVEN recurrences WHEN create text state THEN assert text state is created`(
        recurrences: Recurrences,
        expected: LocalizedText?,
    ) {
        val textState = RecurrencesTextStateFactory.create(recurrences)
        assertEquals(expected, textState)
    }

    companion object {
        @JvmStatic
        fun recurrencesSource(): List<Arguments> {
            val fixedRecurrences = Recurrences.Fixed(DayOfMonth(1))
            val seasonalRecurrences = Recurrences.Seasonal(
                listOf(
                    DayOfYear(1),
                    DayOfYear(31),
                ),
            )
            return listOf(
                arguments(
                    fixedRecurrences,
                    FixedRecurrencesLocalizedText(fixedRecurrences),
                ),
                arguments(
                    seasonalRecurrences,
                    SeasonalRecurrencesLocalizedText(seasonalRecurrences),
                ),
                arguments(
                    Recurrences.Seasonal(emptyList()),
                    null,
                ),
                arguments(Recurrences.Variable, null),
            )
        }
    }
}
