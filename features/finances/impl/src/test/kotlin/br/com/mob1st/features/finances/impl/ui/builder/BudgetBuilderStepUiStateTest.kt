package br.com.mob1st.features.finances.impl.ui.builder

import br.com.mob1st.features.finances.impl.R
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.domain.values.budgetBuilder
import br.com.mob1st.features.finances.impl.domain.values.category
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepUiState.Loaded
import br.com.mob1st.features.finances.impl.ui.utils.components.CategorySectionItemState
import io.kotest.property.Arb
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
        val budgetBuilderStepUiState = Loaded(budgetBuilder)

        // Then
        assertEquals(
            expectedManuallyAdded,
            budgetBuilderStepUiState.manuallyAdded,
        )
        assertEquals(
            expectedSuggestions,
            budgetBuilderStepUiState.suggestions,
        )
    }

    @ParameterizedTest
    @MethodSource("headerSource")
    fun `GIVEN a builder WHEN get header THEN assert header is correct`(
        step: BuilderNextAction.Step,
        expectedHeader: Loaded.Header,
    ) {
        // When
        val header = Loaded(
            BudgetBuilder(step, emptyList()),
        ).header

        // Then
        assertEquals(expectedHeader, header)
    }

    private fun fixtureCategory(isSuggested: Boolean) = Arb.category().map {
        it.copy(isSuggested = isSuggested)
    }

    companion object {
        @JvmStatic
        fun headerSource() = listOf(
            arguments(
                FixedExpensesStep,
                Loaded.Header(
                    title = R.string.finances_builder_fixed_expenses_header,
                    description = R.string.finances_builder_fixed_expenses_subheader,
                ),
            ),
            arguments(
                FixedIncomesStep,
                Loaded.Header(
                    title = R.string.finances_builder_fixed_incomes_header,
                    description = R.string.finances_builder_fixed_incomes_subheader,
                ),
            ),
            arguments(
                VariableExpensesStep,
                Loaded.Header(
                    title = R.string.finances_builder_variable_expenses_header,
                    description = R.string.finances_builder_variable_expenses_subheader,
                ),
            ),
            arguments(
                SeasonalExpensesStep,
                Loaded.Header(
                    title = R.string.finances_builder_seasonal_expenses_title,
                    description = R.string.finances_builder_seasonal_expenses_subheader,
                ),
            ),
        )
    }
}
