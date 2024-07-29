package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.categorySuggestion
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class CategoryTest {
    @ParameterizedTest
    @MethodSource("stepToRecurrencesSource")
    fun `GIVEN a step And a suggestion WHEN create category THEN assert result`(
        step: BuilderNextAction.Step,
        expectedRecurrences: Recurrences,
    ) {
        val suggestion = Arb.categorySuggestion().next()
        val actual = Category.create(step, suggestion)
        val expected = Category(
            id = Category.Id(0),
            name = suggestion.name,
            image = suggestion.image,
            amount = Money.Zero,
            isExpense = step.isExpense,
            isSuggested = true,
            recurrences = expectedRecurrences,
        )
        assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        fun stepToRecurrencesSource() = listOf(
            arguments(
                FixedExpensesStep,
                Recurrences.Fixed(DayOfMonth(1)),
            ),
            arguments(
                SeasonalExpensesStep,
                Recurrences.Seasonal(emptyList()),
            ),
            arguments(
                VariableExpensesStep,
                Recurrences.Variable,
            ),
            arguments(
                FixedIncomesStep,
                Recurrences.Fixed(DayOfMonth(1)),
            ),
        )
    }
}
