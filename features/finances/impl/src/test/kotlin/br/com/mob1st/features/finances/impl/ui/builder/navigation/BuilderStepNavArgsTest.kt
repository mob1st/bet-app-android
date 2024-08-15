package br.com.mob1st.features.finances.impl.ui.builder.navigation

import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNavArgs
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class BuilderStepNavArgsTest {
    @ParameterizedTest
    @MethodSource("stepAndArgsSource")
    fun `GIVEN a step action WHEN create nav args THEN assert it is the expected step args`(
        step: BudgetBuilderAction.Step,
        expected: BuilderStepNavArgs,
    ) {
        val actual = BuilderStepNavArgs.fromStep(step)
        assertEquals(
            expected,
            actual,
        )
    }

    @ParameterizedTest
    @MethodSource("stepAndArgsSource")
    fun `GIVEN a nav args WHEN create the domain step THEN assert it is the expected step`(
        expected: BudgetBuilderAction.Step,
        args: BuilderStepNavArgs,
    ) {
        val actual = args.toStep()
        assertEquals(
            expected,
            actual,
        )
    }

    companion object {
        @JvmStatic
        fun stepAndArgsSource() = listOf(
            arguments(
                FixedExpensesStep,
                BuilderStepNavArgs.FixedExpensesStepArgs,
            ),
            arguments(
                VariableExpensesStep,
                BuilderStepNavArgs.VariableExpensesStepArgs,
            ),
            arguments(
                SeasonalExpensesStep,
                BuilderStepNavArgs.SeasonalExpensesStepArgs,
            ),
            arguments(
                FixedIncomesStep,
                BuilderStepNavArgs.FixedIncomesStepArgs,
            ),
        )
    }
}
