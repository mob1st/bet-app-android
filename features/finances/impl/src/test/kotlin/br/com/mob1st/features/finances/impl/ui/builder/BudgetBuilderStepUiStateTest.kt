package br.com.mob1st.features.finances.impl.ui.builder

import br.com.mob1st.features.finances.impl.R
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.domain.fixtures.budgetBuilder
import br.com.mob1st.features.finances.impl.domain.fixtures.category
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepUiState
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepHeader
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepLoadedBody
import br.com.mob1st.features.finances.impl.ui.category.components.item.CategorySectionItemState
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

internal class BudgetBuilderStepUiStateTest {
    @Test
    fun `GIVEN a category builder with categories WHEN get lists THEN assert lists is correct`() {
        // Given
        val suggestions = fixtureCategory(true).chunked(2..2).next()
        val manuallyAdded = fixtureCategory(false).chunked(2..2).next()
        val budgetBuilder = Arb.budgetBuilder()
            .map { it.copy(categories = suggestions + manuallyAdded) }
            .next()
        val expectedManuallyAdded = persistentListOf(
            CategorySectionItemState(
                category = budgetBuilder.manuallyAdded[0],
            ),
            CategorySectionItemState(
                category = budgetBuilder.manuallyAdded[1],
            ),
        )
        val expectedSuggestions = persistentListOf(
            CategorySectionItemState(
                category = budgetBuilder.suggestions[0],
            ),
            CategorySectionItemState(
                category = budgetBuilder.suggestions[1],
            ),
        )
        // When
        val isLoading = Arb.boolean().next()
        val budgetBuilderStepUiState = BudgetBuilderStepUiState(budgetBuilder, isLoading)
        val actual = budgetBuilderStepUiState.body as BuilderStepLoadedBody
        // Then
        assertEquals(
            expectedManuallyAdded,
            actual.manuallyAdded,
        )
        assertEquals(
            expectedSuggestions,
            actual.suggestions,
        )
        assertEquals(
            isLoading,
            budgetBuilderStepUiState.isLoadingNext,
        )
    }

    @ParameterizedTest
    @MethodSource("headerSource")
    fun `GIVEN a step WHEN get header THEN assert header is correct`(
        step: BudgetBuilderAction.Step,
        expectedHeader: BuilderStepHeader,
    ) {
        // When
        val header = BudgetBuilderStepUiState(step).header

        // Then
        assertEquals(expectedHeader, header)
    }

    @ParameterizedTest
    @MethodSource("headerSource")
    fun `GIVEN a builder WHEN get header THEN assert header is correct`(
        step: BudgetBuilderAction.Step,
        expectedHeader: BuilderStepHeader,
    ) {
        val builder = Arb.budgetBuilder().next().copy(
            id = step,
        )
        val actual = BudgetBuilderStepUiState(builder, Arb.boolean().next())
        assertEquals(expectedHeader, actual.header)
    }

    private fun fixtureCategory(isSuggested: Boolean) = Arb.category().map {
        it.copy(isSuggested = isSuggested)
    }

    companion object {
        @JvmStatic
        fun headerSource() = listOf(
            arguments(
                FixedExpensesStep,
                BuilderStepHeader(
                    title = R.string.finances_builder_fixed_expenses_header,
                    description = R.string.finances_builder_fixed_expenses_subheader,
                ),
            ),
            arguments(
                FixedIncomesStep,
                BuilderStepHeader(
                    title = R.string.finances_builder_fixed_incomes_header,
                    description = R.string.finances_builder_fixed_incomes_subheader,
                ),
            ),
            arguments(
                VariableExpensesStep,
                BuilderStepHeader(
                    title = R.string.finances_builder_variable_expenses_header,
                    description = R.string.finances_builder_variable_expenses_subheader,
                ),
            ),
            arguments(
                SeasonalExpensesStep,
                BuilderStepHeader(
                    title = R.string.finances_builder_step_seasonal_expenses_title,
                    description = R.string.finances_builder_seasonal_expenses_subheader,
                ),
            ),
        )
    }
}
