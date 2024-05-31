package br.com.mob1st.features.finances.impl.data.morphisms

import br.com.mob1st.features.finances.impl.data.preferences.RecurrenceBuilderCompletions
import br.com.mob1st.features.finances.impl.data.ram.RecurrenceBuilderLists
import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceBuilder
import br.com.mob1st.tests.featuresutils.fixture
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RecurrenceBuilderCacheTest {
    @Test
    fun `GIVEN a cache model WHEN map to domain THEN assert fields`() {
        // GIVEN
        val subject = fixture<RecurrenceBuilderCache>()

        // WHEN
        val actual = subject.toDomain()

        // THEN
        val expected =
            RecurrenceBuilder(
                fixedExpensesStep =
                    RecurrenceBuilder.Step(
                        list = subject.lists.fixedExpensesList,
                        isCompleted = subject.completions.isFixedExpansesCompleted,
                    ),
                variableExpensesStep =
                    RecurrenceBuilder.Step(
                        list = subject.lists.variableExpensesList,
                        isCompleted = subject.completions.isVariableExpansesCompleted,
                    ),
                seasonalExpensesStep =
                    RecurrenceBuilder.Step(
                        list = subject.lists.seasonalExpensesList,
                        isCompleted = subject.completions.isSeasonalExpansesCompleted,
                    ),
                incomesStep =
                    RecurrenceBuilder.Step(
                        list = subject.lists.incomesList,
                        isCompleted = subject.completions.isIncomesCompleted,
                    ),
            )
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a domain model WHEN map to cache THEN assert fields`() {
        // GIVEN
        val subject = fixture<RecurrenceBuilder>()

        // WHEN
        val actual = subject.toData()

        // THEN
        val expected =
            RecurrenceBuilderCache(
                completions =
                    RecurrenceBuilderCompletions(
                        isFixedExpansesCompleted = subject.fixedExpensesStep.isCompleted,
                        isVariableExpansesCompleted = subject.variableExpensesStep.isCompleted,
                        isSeasonalExpansesCompleted = subject.seasonalExpensesStep.isCompleted,
                        isIncomesCompleted = subject.incomesStep.isCompleted,
                    ),
                lists =
                    RecurrenceBuilderLists(
                        fixedExpensesList = subject.fixedExpensesStep.list,
                        variableExpensesList = subject.variableExpensesStep.list,
                        seasonalExpensesList = subject.seasonalExpensesStep.list,
                        incomesList = subject.incomesStep.list,
                    ),
            )
        assertEquals(expected, actual)
    }
}
