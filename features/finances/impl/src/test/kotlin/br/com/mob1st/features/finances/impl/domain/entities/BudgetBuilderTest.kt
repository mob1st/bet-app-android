package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.domain.fixtures.category
import br.com.mob1st.features.finances.impl.domain.fixtures.money
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BudgetBuilderTest {
    @Test
    fun `GIVEN a step And enough inputs WHEN calculate remaining items THEN assert difference`() {
        val step = Arb.bind<BudgetBuilderAction.Step>().next()
        val builder = BudgetBuilder(
            id = step,
            categories = categories(
                positiveCount = step.minimumRequiredToProceed,
            ),
        )
        val actual = builder.calculateRemainingInputs()
        assertEquals(0, actual)
    }

    @Test
    fun `GIVEN a step And more than enough inputs WHEN calculate remaining items THEN assert difference`() {
        val step = Arb.bind<BudgetBuilderAction.Step>().next()
        val builder = BudgetBuilder(
            id = step,
            categories = categories(
                positiveCount = step.minimumRequiredToProceed + 1,
            ),
        )
        val actual = builder.calculateRemainingInputs()
        assertEquals(0, actual)
    }

    @Test
    fun `GIVEN a step And not enough categories WHEN get calculate remaining items THEN assert the difference`() {
        val step = Arb.bind<BudgetBuilderAction.Step>().filter { it.minimumRequiredToProceed > 0 }.next()
        val builder = BudgetBuilder(
            id = step,
            categories = categories(
                positiveCount = step.minimumRequiredToProceed - 1,
            ),
        )
        val actual = builder.calculateRemainingInputs()
        assertEquals(1, actual)
    }

    @Test
    fun `WHEN get the first step THEN assert it is fixed expense`() {
        val actual = BudgetBuilder.firstStep()
        assertEquals(VariableExpensesStep, actual)
    }

    @Test
    fun `GIVEN a step and only suggestions WHEN create budget builder THEN assert partition is done`() {
        val step = Arb.bind<BudgetBuilderAction.Step>().next()
        val suggestions = Arb.category().map {
            it.copy(isSuggested = true)
        }.chunked(5..10).next()
        val actual = BudgetBuilder(step, suggestions)
        assertEquals(step, actual.id)
        assertTrue(actual.manuallyAdded.isEmpty())
        assertEquals(suggestions, actual.suggestions)
    }

    @Test
    fun `GIVEN a step and only manually added categories WHEN create budget builder THEN assert partition is done`() {
        val step = Arb.bind<BudgetBuilderAction.Step>().next()
        val manuallyAdded = Arb.category().map {
            it.copy(isSuggested = false)
        }.chunked(5..10).next()
        val actual = BudgetBuilder(step, manuallyAdded)
        assertEquals(step, actual.id)
        assertEquals(manuallyAdded, actual.manuallyAdded)
        assertTrue(actual.suggestions.isEmpty())
    }

    @Test
    fun `GIVEN a step and both manually added and suggested categories WHEN create budget builder THEN assert partition is done`() {
        val step = Arb.bind<BudgetBuilderAction.Step>().next()
        val categories = Arb.category().chunked(5..10).next()
        val expectedManuallyAdded = categories.filter { !it.isSuggested }
        val expectedSuggestions = categories.filter { it.isSuggested }
        val actual = BudgetBuilder(step, categories)
        assertEquals(step, actual.id)
        assertEquals(expectedManuallyAdded, actual.manuallyAdded)
        assertEquals(expectedSuggestions, actual.suggestions)
    }

    @Test
    fun `GIVEN a step and no categories WHEN create budget builder THEN assert partition is done`() {
        val step = Arb.bind<BudgetBuilderAction.Step>().next()
        val actual = BudgetBuilder(step, emptyList())
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
