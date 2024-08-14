package br.com.mob1st.features.finances.impl.domain.events

import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceType
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.infra.data.fixtures.rowId
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.arbs.products.products
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

    @ParameterizedTest
    @MethodSource("categoryDetailSource")
    fun `GIVEN a category intent, recurrence type and is expense falg WHEN create category detail screen view event THEN return correct event key And params`(
        intent: GetCategoryIntent,
        recurrenceType: RecurrenceType,
        isExpense: Boolean,
    ) {
        val actual = AnalyticsEvent.categoryScreenViewEvent(intent, recurrenceType, isExpense)
        val expected = AnalyticsEvent(
            name = "screen_view",
            params = mapOf(
                "screen_name" to "category",
                "intent" to when (intent) {
                    is GetCategoryIntent.Create -> "create"
                    is GetCategoryIntent.Edit -> "edit"
                },
                "name" to intent.name,
                "recurrenceType" to recurrenceType.name,
                "isExpense" to isExpense,
            ),
        )
        assertEquals(expected, actual)
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

        @JvmStatic
        fun categoryDetailSource() = listOf(
            arguments(
                GetCategoryIntent.Create(
                    name = Arb.products().map { it.name }.next(),
                ),
                Arb.enum<RecurrenceType>().next(),
                Arb.boolean().next(),
            ),
            arguments(
                GetCategoryIntent.Edit(
                    id = Category.Id(Arb.rowId().next()),
                    name = Arb.products().map { it.name }.next(),
                ),
                Arb.enum<RecurrenceType>().next(),
                Arb.boolean().next(),
            ),
        )
    }
}
