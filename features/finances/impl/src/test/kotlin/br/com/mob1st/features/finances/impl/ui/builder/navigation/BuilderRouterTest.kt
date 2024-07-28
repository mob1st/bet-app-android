package br.com.mob1st.features.finances.impl.ui.builder.navigation

import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRoute.Step.Type.FixedExpenses
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRoute.Step.Type.FixedIncomes
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRoute.Step.Type.SeasonalExpenses
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRoute.Step.Type.VariableExpenses
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource

class BuilderRouterTest {
    @ParameterizedTest
    @MethodSource("sendActionSource")
    fun `GIVEN a action WHEN send THEN return the next route`(
        action: BuilderNextAction,
        expected: BuilderRoute,
    ) {
        val actual = BuilderRouter.send(action)
        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("receiveRouteSource")
    fun `GIVEN a route WHEN receive THEN return the next action`(
        route: BuilderRoute.Step,
        expected: BuilderNextAction.Step,
    ) {
        val actual = BuilderRouter.receive(route)
        assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        fun sendActionSource() = listOf(
            arguments(
                FixedExpensesStep,
                BuilderRoute.Step(FixedExpenses),
            ),
            arguments(
                VariableExpensesStep,
                BuilderRoute.Step(VariableExpenses),
            ),
            arguments(
                SeasonalExpensesStep,
                BuilderRoute.Step(SeasonalExpenses),
            ),
            arguments(
                FixedIncomesStep,
                BuilderRoute.Step(FixedIncomes),
            ),
            arguments(
                BuilderNextAction.Complete,
                BuilderRoute.Completion,
            ),
        )

        @JvmStatic
        fun receiveRouteSource() = listOf(
            arguments(
                BuilderRoute.Step(FixedExpenses),
                FixedExpensesStep,
            ),
            arguments(
                BuilderRoute.Step(VariableExpenses),
                VariableExpensesStep,
            ),
            arguments(
                BuilderRoute.Step(SeasonalExpenses),
                SeasonalExpensesStep,
            ),
            arguments(
                BuilderRoute.Step(FixedIncomes),
                FixedIncomesStep,
            ),
        )
    }
}
