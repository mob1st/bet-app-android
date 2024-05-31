package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.fakes.FakeRecurrenceBuilderRepository
import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItem
import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItemGroup
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import br.com.mob1st.tests.featuresutils.fixture
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetBudgetItemGroupTest {
    private lateinit var recurrenceBuilderRepository: FakeRecurrenceBuilderRepository

    @BeforeEach
    fun setUp() {
        recurrenceBuilderRepository = FakeRecurrenceBuilderRepository()
    }

    @Test
    fun `GIVEN a builder WHEN get THEN return the list of fixed expenses`() =
        runTest {
            // GIVEN
            val fixture =
                fixture<RecurrentCategory>().copy(
                    amount = Money(100),
                    type = BudgetItem.Type.EXPENSE,
                )
            val expected =
                BudgetItemGroup(
                    items =
                        listOf(
                            BudgetItemGroup.ProportionalItem(
                                item = fixture,
                                proportion = 100,
                            ),
                        ),
                    summaries =
                        BudgetItemGroup.Summaries(
                            incomes = Money.Zero,
                            expenses = Money(100),
                            balance = Money(-100),
                        ),
                )
            recurrenceBuilderRepository.getSetState.value =
                fixture<RecurrenceBuilder>().copy(
                    variableExpensesStep =
                        RecurrenceBuilder.Step(
                            list = listOf(fixture).toPersistentList(),
                            isCompleted = false,
                        ),
                )

            // WHEN
            val actual =
                recurrenceBuilderRepository.getBudgetItemGroup {
                    it.variableExpensesStep
                }.first()

            // THEN
            assertEquals(expected, actual)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN two builder emissions with the exact same list for a step WHEN get the budget group by the step THEN should emit only once`() =
        runTest {
            // this test ensures that the distinctUntilChanged operator is applied in the flow to avoid unnecessary emissions

            // GIVEN
            val item =
                fixture<RecurrentCategory>().copy(
                    type = BudgetItem.Type.INCOME,
                )
            val builder =
                fixture<RecurrenceBuilder>().copy(
                    incomesStep =
                        RecurrenceBuilder.Step(
                            list = listOf(item).toPersistentList(),
                            isCompleted = false,
                        ),
                )

            // WHEN
            val expectedList = mutableListOf<BudgetItemGroup<RecurrentCategory>>()
            val job =
                launch {
                    recurrenceBuilderRepository.getBudgetItemGroup {
                        it.incomesStep
                    }.toList(expectedList)
                }

            recurrenceBuilderRepository.getSetState.value = builder
            advanceUntilIdle()

            recurrenceBuilderRepository.getSetState.value =
                builder.copy(
                    incomesStep =
                        builder.incomesStep.copy(
                            // change a field that doesn't affect the number of emissions
                            isCompleted = true,
                        ),
                )
            advanceUntilIdle()

            // THEN
            assertEquals(1, expectedList.size)
            job.cancel()
        }
}
