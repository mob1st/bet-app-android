package br.com.mob1st.features.finances.impl.domain.events

import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNavArgs
import br.com.mob1st.features.finances.impl.ui.category.detail.CategoryDetailArgs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource

internal class ScreenViewEventsTest {
    @ParameterizedTest
    @MethodSource("builderStepToEventSource")
    fun `GIVEN a step WHEN create screen view event THEN return the correct event key`(
        args: BuilderStepNavArgs,
        expectedEventKey: String,
    ) {
        val actual = AnalyticsEvent.builderStepScreenView(args)
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
        args: CategoryDetailArgs,
        expectedIntent: String,
    ) {
        val actual = AnalyticsEvent.categoryScreenViewEvent(args)
        val expected = AnalyticsEvent(
            name = "screen_view",
            params = mapOf(
                "screen_name" to "category",
                "intent" to expectedIntent,
                "name" to args.name,
                "recurrenceType" to args.recurrenceType.name,
                "isExpense" to args.isExpense,
            ),
        )
        assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        fun builderStepToEventSource() = listOf(
            arguments(
                BuilderStepNavArgs.FixedExpensesStepArgs,
                "builder_fixed_expenses",
            ),
            arguments(
                BuilderStepNavArgs.VariableExpensesStepArgs,
                "builder_variable_expenses",
            ),
            arguments(
                BuilderStepNavArgs.FixedIncomesStepArgs,
                "builder_fixed_incomes",
            ),
            arguments(
                BuilderStepNavArgs.SeasonalExpensesStepArgs,
                "builder_seasonal_expenses",
            ),
        )

        @JvmStatic
        fun categoryDetailSource() = listOf(
            arguments(
                Arb.bind<CategoryDetailArgs>().map {
                    it.copy(
                        intent = CategoryDetailArgs.Intent.Edit(
                            id = Arb.long().next(),
                        ),
                    )
                }.next(),
                "edit",
            ),
            arguments(
                Arb.bind<CategoryDetailArgs>().map {
                    it.copy(
                        intent = CategoryDetailArgs.Intent.Create,
                    )
                }.next(),
                "create",
            ),
        )
    }
}
