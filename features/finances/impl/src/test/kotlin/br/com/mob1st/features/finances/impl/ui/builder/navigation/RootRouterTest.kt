package br.com.mob1st.features.finances.impl.ui.builder.navigation

import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroNextStepNavEvent
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderNavRoute.Step.Id.FixedExpenses
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderNavRoute.Step.Id.FixedIncomes
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderNavRoute.Step.Id.SeasonalExpenses
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderNavRoute.Step.Id.VariableExpenses
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNextNavEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource

class RootRouterTest {
    @ParameterizedTest
    @MethodSource("stepActionEventSource")
    fun `GIVEN a action WHEN send THEN return the next route`(
        action: BudgetBuilderAction,
        expected: BuilderNavRoute,
    ) {
        val event = BuilderStepNextNavEvent(action)
        val actual = BuilderRouter.route(event)
        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("introActionEventSource")
    fun `GIVEN a action WHEN send THEN return the next route`(
        action: BudgetBuilderAction.Step,
        expected: BuilderNavRoute,
    ) {
        val event = BuilderIntroNextStepNavEvent(action)
        val actual = BuilderRouter.route(event)
        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("receiveRouteSource")
    fun `GIVEN a route WHEN receive THEN return the next action`(
        route: BuilderNavRoute.Step,
        expected: BudgetBuilderAction.Step,
    ) {
        val actual = BuilderRouter.receive(route)
        assertEquals(expected, actual)
    }

    companion object {
        private val stepArguments = listOf(
            arguments(
                FixedExpensesStep,
                BuilderNavRoute.Step(FixedExpenses),
            ),
            arguments(
                VariableExpensesStep,
                BuilderNavRoute.Step(VariableExpenses),
            ),
            arguments(
                SeasonalExpensesStep,
                BuilderNavRoute.Step(SeasonalExpenses),
            ),
            arguments(
                FixedIncomesStep,
                BuilderNavRoute.Step(FixedIncomes),
            ),
        )

        @JvmStatic
        fun stepActionEventSource() = stepArguments + arguments(
            BudgetBuilderAction.Complete,
            BuilderNavRoute.Completion(),
        )

        @JvmStatic
        fun introActionEventSource() = stepArguments

        @JvmStatic
        fun receiveRouteSource() = listOf(
            arguments(
                BuilderNavRoute.Step(FixedExpenses),
                FixedExpensesStep,
            ),
            arguments(
                BuilderNavRoute.Step(VariableExpenses),
                VariableExpensesStep,
            ),
            arguments(
                BuilderNavRoute.Step(SeasonalExpenses),
                SeasonalExpensesStep,
            ),
            arguments(
                BuilderNavRoute.Step(FixedIncomes),
                FixedIncomesStep,
            ),
        )
    }
}
