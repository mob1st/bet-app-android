package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayAndMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfWeek
import br.com.mob1st.features.finances.impl.domain.values.Month
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class DeleteRecurrenceMapperTest {
    @ParameterizedTest
    @ArgumentsSource(RecurrenceAndIndexProvider::class)
    fun `GIVEN a recurrence AND an index WHEN map THEN assert result`(
        recurrences: Recurrences,
        recurrenceIndex: Int,
        expected: Pair<Int, Int>,
    ) {
        val result = DeleteRecurrenceMapper.map(recurrences, recurrenceIndex)
        assertEquals(expected, result)
    }

    object RecurrenceAndIndexProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    Recurrences.Fixed(
                        daysOfMonth = listOf(
                            DayOfMonth(1),
                            DayOfMonth(2),
                            DayOfMonth(3),
                        ),
                    ),
                    0,
                    Pair(1, 0),
                ),
                Arguments.of(
                    Recurrences.Variable(
                        daysOfWeek = listOf(
                            DayOfWeek(1),
                            DayOfWeek(2),
                            DayOfWeek(3),
                        ),
                    ),
                    1,
                    Pair(2, 0),
                ),
                Arguments.of(
                    Recurrences.Seasonal(
                        daysAndMonths = listOf(
                            DayAndMonth(DayOfMonth(1), Month(1)),
                            DayAndMonth(DayOfMonth(1), Month(2)),
                            DayAndMonth(DayOfMonth(1), Month(3)),
                        ),
                    ),
                    2,
                    Pair(1, 3),
                ),
            )
        }
    }
}
