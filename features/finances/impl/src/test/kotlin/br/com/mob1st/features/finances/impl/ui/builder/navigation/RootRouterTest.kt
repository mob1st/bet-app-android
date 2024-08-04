package br.com.mob1st.features.finances.impl.ui.builder.navigation

import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderNavRoute.Step.Type.FixedExpenses
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderNavRoute.Step.Type.FixedIncomes
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderNavRoute.Step.Type.SeasonalExpenses
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderNavRoute.Step.Type.VariableExpenses
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource

class RootRouterTest {
    @ParameterizedTest
    @MethodSource("sendActionSource")
    fun `GIVEN a action WHEN send THEN return the next route`(
        action: BuilderNextAction,
        expected: BuilderNavRoute,
    ) {
        val actual = BuilderRouter.to(action)
        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("receiveRouteSource")
    fun `GIVEN a route WHEN receive THEN return the next action`(
        route: BuilderNavRoute.Step,
        expected: BuilderNextAction.Step,
    ) {
        val actual = BuilderRouter.from(route)
        assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        fun sendActionSource() = listOf(
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
            arguments(
                BuilderNextAction.Complete,
                BuilderNavRoute.Completion,
            ),
        )

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
