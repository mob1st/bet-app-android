package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.domain.fixtures.category
import br.com.mob1st.features.finances.impl.domain.fixtures.money
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BudgetBuilderTest {
    @Test
    fun `GIVEN a step And enough manually added categories WHEN get next action THEN assert next action`() {
        // here
        val step = Arb.bind<BuilderNextAction.Step>().next()
        val builder = BudgetBuilder(
            id = step,
            manuallyAdded = categories(
                positiveCount = step.minimumRequiredToProceed,
            ),
            suggestions = categories(),
        )
        val actual = builder.next()
        assertEquals(step.next, actual)
    }

    @Test
    fun `GIVEN a step And enough suggested categories WHEN get next action THEN assert next action`() {
        val step = Arb.bind<BuilderNextAction.Step>().next()
        val builder = BudgetBuilder(
            id = step,
            manuallyAdded = categories(),
            suggestions = categories(
                positiveCount = step.minimumRequiredToProceed,
            ),
        )
        val actual = builder.next()
        assertEquals(step.next, actual)
    }

    @Test
    fun `GIVEN a step And enough manually added and suggested categories WHEN get next action THEN assert next action`() {
        val step = Arb.bind<BuilderNextAction.Step>().next()
        val builder = BudgetBuilder(
            id = step,
            manuallyAdded = categories(
                positiveCount = step.minimumRequiredToProceed,
            ),
            suggestions = categories(
                positiveCount = step.minimumRequiredToProceed,
            ),
        )
        val actual = builder.next()
        assertEquals(step.next, actual)
    }

    @Test
    fun `GIVEN a step And more than enough inputs WHEN get next action THEN assert next action`() {
        val step = Arb.bind<BuilderNextAction.Step>().next()
        val builder = BudgetBuilder(
            id = step,
            manuallyAdded = categories(
                positiveCount = step.minimumRequiredToProceed + 1,
            ),
            suggestions = categories(
                positiveCount = step.minimumRequiredToProceed + 1,
            ),
        )
        val actual = builder.next()
        assertEquals(step.next, actual)
    }

    @Test
    fun `GIVEN a step And not enough manual inputs WHEN get next action THEN assert exception`() {
        val step = Arb.bind<BuilderNextAction.Step>().next()
        val builder = BudgetBuilder(
            id = step,
            manuallyAdded = categories(
                positiveCount = step.minimumRequiredToProceed - 1,
            ),
            suggestions = categories(),
        )
        val exception = assertThrows<NotEnoughInputsException> {
            builder.next()
        }
        assertEquals(1, exception.remainingInputs)
    }

    @Test
    fun `GIVEN a step And not enough suggested inputs WHEN get next action THEN assert exception`() {
        val step = Arb.bind<BuilderNextAction.Step>().next()
        val builder = BudgetBuilder(
            id = step,
            manuallyAdded = categories(),
            suggestions = categories(
                positiveCount = step.minimumRequiredToProceed - 1,
            ),
        )
        val exception = assertThrows<NotEnoughInputsException> {
            builder.next()
        }
        assertEquals(1, exception.remainingInputs)
    }

    @Test
    fun `GIVEN a step And only zeroed categories WHEN get next action THEN assert exception`() {
        val step = Arb.bind<BuilderNextAction.Step>().next()
        val builder = BudgetBuilder(
            id = step,
            manuallyAdded = categories(),
            suggestions = categories(),
        )
        val exception = assertThrows<NotEnoughInputsException> {
            builder.next()
        }
        assertEquals(step.minimumRequiredToProceed, exception.remainingInputs)
    }

    @Test
    fun `WHEN get the first step THEN assert it is fixed expense`() {
        val actual = BudgetBuilder.firstStep()
        assertEquals(FixedExpensesStep, actual)
    }

    @Test
    fun `GIVEN a step WHEN get next THEN assert it is expected`() {
        assertEquals(VariableExpensesStep, FixedExpensesStep.next)
        assertEquals(FixedIncomesStep, VariableExpensesStep.next)
        assertEquals(BuilderNextAction.Complete, FixedIncomesStep.next)
    }

    @Test
    fun `GIVEN a step and only suggestions WHEN create budget builder THEN assert partition is done`() {
        val step = Arb.bind<BuilderNextAction.Step>().next()
        val suggestions = Arb.category().map {
            it.copy(isSuggested = true)
        }.chunked(5..10).next()
        val actual = BudgetBuilder.create(step, suggestions)
        assertEquals(step, actual.id)
        assertTrue(actual.manuallyAdded.isEmpty())
        assertEquals(suggestions, actual.suggestions)
    }

    @Test
    fun `GIVEN a step and only manually added categories WHEN create budget builder THEN assert partition is done`() {
        val step = Arb.bind<BuilderNextAction.Step>().next()
        val manuallyAdded = Arb.category().map {
            it.copy(isSuggested = false)
        }.chunked(5..10).next()
        val actual = BudgetBuilder.create(step, manuallyAdded)
        assertEquals(step, actual.id)
        assertEquals(manuallyAdded, actual.manuallyAdded)
        assertTrue(actual.suggestions.isEmpty())
    }

    @Test
    fun `GIVEN a step and both manually added and suggested categories WHEN create budget builder THEN assert partition is done`() {
        val step = Arb.bind<BuilderNextAction.Step>().next()
        val categories = Arb.category().chunked(5..10).next()
        val expectedManuallyAdded = categories.filter { !it.isSuggested }
        val expectedSuggestions = categories.filter { it.isSuggested }
        val actual = BudgetBuilder.create(step, categories)
        assertEquals(step, actual.id)
        assertEquals(expectedManuallyAdded, actual.manuallyAdded)
        assertEquals(expectedSuggestions, actual.suggestions)
    }

    @Test
    fun `GIVEN a step and no categories WHEN create budget builder THEN assert partition is done`() {
        val step = Arb.bind<BuilderNextAction.Step>().next()
        val actual = BudgetBuilder.create(step, emptyList())
        assertEquals(step, actual.id)
        assertTrue(actual.manuallyAdded.isEmpty())
        assertTrue(actual.suggestions.isEmpty())
    }

    companion object {
        private fun categories(
            positiveCount: Int = 0,
        ): List<Category> {
            val sample = Arb
                .category()
                .map { it.copy(amount = Money.Zero) }
                .chunked(5..10)
                .next()
                .toMutableList()
            while (sample.count { it.amount.cents > 0 } < positiveCount) {
                val randomIndex = sample.indexOf(sample.random())
                sample[randomIndex] = sample[randomIndex].copy(
                    amount = Arb.money().next(),
                )
            }
            return sample
        }
    }
}
