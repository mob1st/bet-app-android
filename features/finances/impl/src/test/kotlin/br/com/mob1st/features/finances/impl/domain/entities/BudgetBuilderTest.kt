package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.utils.moduleFixture
import com.appmattus.kotlinfixture.Fixture
import com.appmattus.kotlinfixture.decorator.nullability.AlwaysNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class BudgetBuilderTest {
    @Test
    fun `GIVEN a step And enough manually added categories WHEN get next action THEN assert next action`() {
        val step = steps.random()
        val builder = BudgetBuilder(
            id = step,
            manuallyAdded = categories(positiveCount = step.minimumRequiredToProceed),
            suggestions = suggestions(withoutCategoryCount = 0),
        )
        val actual = builder.next()
        assertEquals(step.next, actual)
    }

    @Test
    fun `GIVEN a step And enough suggested categories WHEN get next action THEN assert next action`() {
        val step = steps.random()
        val builder = BudgetBuilder(
            id = step,
            manuallyAdded = categories(),
            suggestions = suggestions(
                positiveCount = step.minimumRequiredToProceed,
                withoutCategoryCount = 3,
            ),
        )
        val actual = builder.next()
        assertEquals(step.next, actual)
    }

    @Test
    fun `GIVEN a step And enough manually added and suggested categories WHEN get next action THEN assert next action`() {
        val step = steps.random()
        val builder = BudgetBuilder(
            id = step,
            manuallyAdded = categories(
                positiveCount = step.minimumRequiredToProceed,
            ),
            suggestions = suggestions(
                positiveCount = step.minimumRequiredToProceed,
                withoutCategoryCount = 2,
            ),
        )
        val actual = builder.next()
        assertEquals(step.next, actual)
    }

    @Test
    fun `GIVEN a step And more than enough inputs WHEN get next action THEN assert next action`() {
        val step = steps.random()
        val builder = BudgetBuilder(
            id = step,
            manuallyAdded = categories(
                positiveCount = step.minimumRequiredToProceed + 1,
            ),
            suggestions = suggestions(
                positiveCount = step.minimumRequiredToProceed + 1,
                withoutCategoryCount = 9,
            ),
        )
        val actual = builder.next()
        assertEquals(step.next, actual)
    }

    @Test
    fun `GIVEN a step And not enough manual inputs WHEN get next action THEN assert exception`() {
        val step = steps.random()
        val builder = BudgetBuilder(
            id = step,
            manuallyAdded = categories(
                positiveCount = step.minimumRequiredToProceed - 1,
            ),
            suggestions = suggestions(
                withoutCategoryCount = 10,
            ),
        )
        val exception = assertThrows<NotEnoughInputsException> {
            builder.next()
        }
        assertEquals(1, exception.remainingInputs)
    }

    @Test
    fun `GIVEN a step And not enough suggested inputs WHEN get next action THEN assert exception`() {
        val step = steps.random()
        val builder = BudgetBuilder(
            id = step,
            manuallyAdded = categories(),
            suggestions = suggestions(
                positiveCount = step.minimumRequiredToProceed - 1,
                withoutCategoryCount = 3,
            ),
        )
        val exception = assertThrows<NotEnoughInputsException> {
            builder.next()
        }
        assertEquals(1, exception.remainingInputs)
    }

    @Test
    fun `GIVEN a step And zeroed categories WHEN get next action THEN assert exception`() {
        val step = steps.random()
        val builder = BudgetBuilder(
            id = step,
            manuallyAdded = categories(
                positiveCount = step.minimumRequiredToProceed - 1,
            ),
            suggestions = suggestions(
                zeroedCount = step.minimumRequiredToProceed,
                withoutCategoryCount = 5,
            ),
        )
        val exception = assertThrows<NotEnoughInputsException> {
            builder.next()
        }
        assertEquals(1, exception.remainingInputs)
    }

    @Test
    fun `WHEN get the first step THEN assert it is fixed expense`() {
        val actual = BudgetBuilder.firstStep()
        assertEquals(FixedExpensesStep, actual)
    }

    @Test
    fun `GIVEN a step WHEN get next THEN assert is expected`() {
        assertEquals(VariableExpensesStep, FixedExpensesStep.next)
        assertEquals(FixedIncomesStep, VariableExpensesStep.next)
        assertEquals(BuilderNextAction.Complete, FixedIncomesStep.next)
    }

    companion object {
        private val steps = listOf(
            FixedExpensesStep,
            VariableExpensesStep,
            FixedIncomesStep,
        )

        private fun categories(
            positiveCount: Int = 0,
            zeroedCount: Int = 0,
        ): List<Category> {
            val positiveCountList = positiveInputsFixture(positiveCount).invoke<List<Category>>()
            val zeroedCountList = zeroedInputsFixture(zeroedCount).invoke<List<Category>>()
            return (positiveCountList + zeroedCountList).shuffled()
        }

        private fun suggestions(
            positiveCount: Int = 0,
            zeroedCount: Int = 0,
            withoutCategoryCount: Int = 0,
        ): List<CategorySuggestion> {
            val withoutCategoryList = moduleFixture<List<CategorySuggestion>> {
                nullabilityStrategy(AlwaysNullStrategy)
                repeatCount { withoutCategoryCount }
            }
            val positiveCountList = positiveInputsFixture(positiveCount).invoke<List<CategorySuggestion>>()
            val zeroedCountList = zeroedInputsFixture(zeroedCount).invoke<List<CategorySuggestion>>()
            return (withoutCategoryList + positiveCountList + zeroedCountList).shuffled()
        }

        private fun positiveInputsFixture(count: Int): Fixture = moduleFixture.new {
            repeatCount { count }
            nullabilityStrategy(NeverNullStrategy)
            factory<Money> { Money((1L..500000L).random()) }
            factory<CategorySuggestion> {
                CategorySuggestion(
                    id = fixture(),
                    nameResId = fixture(),
                    linkedCategory = fixture<Category>().copy(
                        amount = fixture(),
                    ),
                )
            }
        }

        private fun zeroedInputsFixture(
            count: Int,
        ): Fixture = moduleFixture.new {
            repeatCount { count }
            nullabilityStrategy(NeverNullStrategy)
            factory<Money> { Money.Zero }
            factory<CategorySuggestion> {
                CategorySuggestion(
                    id = fixture(),
                    nameResId = fixture(),
                    linkedCategory = fixture<Category>().copy(
                        amount = fixture(),
                    ),
                )
            }
        }
    }
}
