package br.com.mob1st.features.finances.impl.ui.utils.texts

import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayAndMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.Month
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class RecurrencesTextStateFactoryTest {
    @ParameterizedTest
    @ArgumentsSource(RecurrencesProvider::class)
    fun `GIVEN recurrences WHEN create text state THEN assert text state is created`(
        recurrences: Recurrences,
        expected: TextState?,
    ) {
        val textState = RecurrencesTextStateFactory.create(recurrences)
        assertEquals(expected, textState)
    }

    object RecurrencesProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            val fixedRecurrences = Recurrences.Fixed(DayOfMonth(1))
            val seasonalRecurrences = Recurrences.Seasonal(
                listOf(
                    DayAndMonth(DayOfMonth(1), Month.February),
                    DayAndMonth(DayOfMonth(31), Month.October),
                ),
            )
            return Stream.of(
                Arguments.of(
                    fixedRecurrences,
                    FixedRecurrencesTextState(fixedRecurrences),
                ),
                Arguments.of(
                    seasonalRecurrences,
                    SeasonalRecurrencesTextState(seasonalRecurrences),
                ),
                Arguments.of(Recurrences.Variable, null),
            )
        }
    }
}
