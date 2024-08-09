package br.com.mob1st.features.finances.impl.domain.events

import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource

class ScreenViewEventsTest {
    @ParameterizedTest
    @MethodSource("builderStepToEventSource")
    fun `GIVEN a step WHEN create screen view event THEN return the correct event key`(
        step: BudgetBuilderAction.Step,
        expectedEventKey: String,
    ) {
        val actual = AnalyticsEvent.builderStepScreenView(step)
        assertEquals(
            AnalyticsEvent(
                name = "screen_view",
                params = mapOf("screen_name" to expectedEventKey),
            ),
            actual,
        )
    }

    @Test
    fun `WHEN create intro screen view event THEN return the correct event key`() {
        val actual = AnalyticsEvent.builderIntroScreenViewEvent()
        assertEquals(
            AnalyticsEvent(
                name = "screen_view",
                params = mapOf("screen_name" to "builder_intro"),
            ),
            actual,
        )
    }

    fun test() {
        // test sheet event
        assert(false)
    }

    companion object {
        @JvmStatic
        fun builderStepToEventSource() = listOf(
            arguments(
                FixedExpensesStep,
                "builder_fixed_expenses",
            ),
            arguments(
                VariableExpensesStep,
                "builder_variable_expenses",
            ),
            arguments(
                FixedIncomesStep,
                "builder_fixed_incomes",
            ),
            arguments(
                SeasonalExpensesStep,
                "builder_seasonal_expenses",
            ),
        )
    }
}
