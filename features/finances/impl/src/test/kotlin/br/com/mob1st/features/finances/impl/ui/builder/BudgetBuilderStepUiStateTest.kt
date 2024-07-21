package br.com.mob1st.features.finances.impl.ui.builder

import br.com.mob1st.features.finances.impl.R
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.ui.builder.steps.AddCategoryListItemState
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepUiState.Loaded
import br.com.mob1st.features.finances.impl.ui.builder.steps.ManualCategorySectionItemState
import br.com.mob1st.features.finances.impl.ui.builder.steps.SuggestionSectionItemState
import br.com.mob1st.features.finances.impl.utils.moduleFixture
import com.appmattus.kotlinfixture.Fixture
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertEquals

internal class BudgetBuilderStepUiStateTest {
    private lateinit var fixture: Fixture

    @BeforeEach
    fun setUp() {
        fixture = moduleFixture.new {
            factory<BudgetBuilder> {
                BudgetBuilder(
                    id = fixture(),
                    manuallyAdded = fixture {
                        repeatCount { 2 }
                    },
                    suggestions = fixture {
                        repeatCount { 2 }
                    },
                )
            }
        }
    }

    @Test
    fun `GIVEN a category builder with categories WHEN get lists THEN assert lists is correct`() {
        // Given
        val budgetBuilder = fixture<BudgetBuilder>()
        val expectedManuallyAdded = persistentListOf(
            ManualCategorySectionItemState(
                category = budgetBuilder.manuallyAdded[0],
            ),
            ManualCategorySectionItemState(
                category = budgetBuilder.manuallyAdded[1],
            ),
            AddCategoryListItemState,
        )
        val expectedSuggestions = persistentListOf(
            SuggestionSectionItemState(
                suggestion = budgetBuilder.suggestions[0],
            ),
            SuggestionSectionItemState(
                suggestion = budgetBuilder.suggestions[1],
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

    @Test
    fun `GIVEN a category builder without categories WHEN get lists THEN assert lists is empty`() {
        // Given
        val budgetBuilder = fixture<BudgetBuilder>().copy(
            manuallyAdded = emptyList(),
            suggestions = emptyList(),
        )
        val expectedManuallyAdded = persistentListOf(AddCategoryListItemState)
        val expectedSuggestions = persistentListOf<SuggestionSectionItemState>()
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
    @ArgumentsSource(StepToHeaderProvider::class)
    fun `GIVEN a builder WHEN get header THEN assert header is correct`(
        step: BuilderNextAction.Step,
        expectedHeader: Loaded.Header,
    ) {
        // When
        val header = Loaded(
            BudgetBuilder(step, emptyList(), emptyList()),
        ).header

        // Then
        assertEquals(expectedHeader, header)
    }

    object StepToHeaderProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    FixedExpensesStep,
                    Loaded.Header(
                        title = R.string.finances_builder_fixed_expenses_header,
                        description = R.string.finances_builder_fixed_expenses_subheader,
                    ),
                ),
                Arguments.of(
                    FixedIncomesStep,
                    Loaded.Header(
                        title = R.string.finances_builder_fixed_incomes_header,
                        description = R.string.finances_builder_fixed_incomes_subheader,
                    ),
                ),
                Arguments.of(
                    VariableExpensesStep,
                    Loaded.Header(
                        title = R.string.finances_builder_variable_expenses_header,
                        description = R.string.finances_builder_variable_expenses_subheader,
                    ),
                ),
            )
        }
    }
}
