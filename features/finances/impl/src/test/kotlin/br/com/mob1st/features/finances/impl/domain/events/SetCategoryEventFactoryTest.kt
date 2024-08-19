package br.com.mob1st.features.finances.impl.domain.events

import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.fixtures.category
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfYear
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.positiveLong
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

internal class SetCategoryEventFactoryTest {
    @ParameterizedTest
    @MethodSource("recurrencesSource")
    fun `GIVEN a recurrence WHEN create event THEN assert properties`(
        id: Long,
        recurrences: Recurrences,
        expectedKey: String,
        expectedValue: Any,
        isNew: Boolean,
    ) {
        val category = Arb.category().next().copy(
            id = Category.Id(id),
            recurrences = recurrences,
        )
        val event = SetCategoryEventFactory.create(category)
        val expected = AnalyticsEvent(
            name = "category_sent",
            params = mapOf(
                "name" to category.name,
                "amount" to category.amount.cents,
                "is_expense" to category.isExpense,
                "is_suggested" to category.isSuggested,
                "create" to isNew,
                expectedKey to expectedValue,
            ),
        )
        assertEquals(expected, event)
    }

    companion object {
        @JvmStatic
        fun recurrencesSource() = listOf(
            arguments(
                Arb.positiveLong().next(),
                Recurrences.Fixed(DayOfMonth(1)),
                "fixed",
                1,
                false,
            ),
            arguments(
                Arb.positiveLong().next(),
                Recurrences.Variable,
                "variable",
                "weekly",
                false,
            ),
            arguments(
                Arb.positiveLong().next(),
                Recurrences.Seasonal(
                    listOf(
                        DayOfYear(1),
                        DayOfYear(2),
                    ),
                ),
                "seasonal",
                "1,2",
                false,
            ),
            arguments(
                Arb.positiveLong().next(),
                Recurrences.Seasonal(
                    listOf(
                        DayOfYear(1),
                    ),
                ),
                "seasonal",
                "1",
                false,
            ),
            arguments(
                Arb.positiveLong().next(),
                Recurrences.Seasonal(
                    emptyList(),
                ),
                "seasonal",
                "",
                false,
            ),
            arguments(
                0L,
                Recurrences.Fixed(DayOfMonth(1)),
                "fixed",
                1,
                true,
            ),
        )
    }
}
