package br.com.mob1st.features.finances.impl.domain.entities

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class RootNextActionStepTest {
    @ParameterizedTest
    @MethodSource("stepSource")
    fun `GIVEN a step WHEN get next THEN assert is expected`(
        step: BuilderNextAction.Step,
        expectedIsExpense: Boolean,
        expectedMinimumRequiredToProceed: Int,
        expectedType: RecurrenceType,
        expectedNext: BuilderNextAction,
    ) {
        assertEquals(
            expectedIsExpense,
            step.isExpense,
        )
        assertEquals(
            expectedMinimumRequiredToProceed,
            step.minimumRequiredToProceed,
        )
        assertEquals(
            expectedType,
            step.type,
        )
        assertEquals(
            expectedNext,
            step.next,
        )
    }

    companion object {
        @JvmStatic
        fun stepSource() = listOf(
            arguments(
                VariableExpensesStep,
                true,
                2,
                RecurrenceType.Variable,
                FixedExpensesStep,
            ),
            arguments(
                FixedExpensesStep,
                true,
                3,
                RecurrenceType.Fixed,
                SeasonalExpensesStep,
            ),
            arguments(
                SeasonalExpensesStep,
                true,
                0,
                RecurrenceType.Seasonal,
                FixedIncomesStep,
            ),
            arguments(
                FixedIncomesStep,
                false,
                0,
                RecurrenceType.Fixed,
                BuilderNextAction.Complete,
            ),
        )
    }
}
