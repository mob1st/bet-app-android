package br.com.mob1st.features.finances.impl.data.repositories

import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.finances.impl.data.preferences.RecurrenceBuilderCompletions
import br.com.mob1st.features.finances.impl.data.ram.RecurrenceBuilderLists
import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceBuilder
import br.com.mob1st.tests.featuresutils.FakeUnitaryDataSource
import br.com.mob1st.tests.featuresutils.fixture
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class RecurrenceBuilderRepositoryImplTest {

    private lateinit var subject: RecurrenceBuilderRepositoryImpl

    private lateinit var listsDataSource: FakeUnitaryDataSource<RecurrenceBuilderLists>
    private lateinit var completionsDataSource: FakeUnitaryDataSource<RecurrenceBuilderCompletions>

    @BeforeEach
    fun setUp() {
        listsDataSource = FakeUnitaryDataSource(
            MutableStateFlow(fixture())
        )
        completionsDataSource = FakeUnitaryDataSource(
            MutableStateFlow(fixture())
        )
        subject = RecurrenceBuilderRepositoryImpl(
            listsDataSource = listsDataSource,
            completionsDataSource = completionsDataSource,
            io = IoCoroutineDispatcher(UnconfinedTestDispatcher())
        )
    }

    @Test
    fun `GIVEN the lists of recurrences and their completion status WHEN get builder THEN combine all of them`() = runTest {
        val actual = subject.get().first()
        assertEquals(
            expected = RecurrenceBuilder(
                fixedExpensesStep = RecurrenceBuilder.Step(
                    list = listsDataSource.setState.value!!.fixedExpensesList,
                    isCompleted = completionsDataSource.setState.value!!.isFixedExpansesCompleted
                ),
                variableExpensesStep = RecurrenceBuilder.Step(
                    list = listsDataSource.setState.value!!.variableExpensesList,
                    isCompleted = completionsDataSource.setState.value!!.isVariableExpansesCompleted
                ),
                seasonalExpensesStep = RecurrenceBuilder.Step(
                    list = listsDataSource.setState.value!!.seasonalExpensesList,
                    isCompleted = completionsDataSource.setState.value!!.isSeasonalExpansesCompleted
                ),
                incomesStep = RecurrenceBuilder.Step(
                    list = listsDataSource.setState.value!!.incomesList,
                    isCompleted = completionsDataSource.setState.value!!.isIncomesCompleted
                )
            ),
            actual = actual
        )
    }

    @Test
    fun `GIVEN the lists of recurrences and their completion status THEN set the lists of each step`() = runTest {
        val expected = fixture<RecurrenceBuilder>()
        subject.set(expected)
        assertEquals(
            expected = RecurrenceBuilderCompletions(
                isFixedExpansesCompleted = expected.fixedExpensesStep.isCompleted,
                isVariableExpansesCompleted = expected.variableExpensesStep.isCompleted,
                isSeasonalExpansesCompleted = expected.seasonalExpensesStep.isCompleted,
                isIncomesCompleted = expected.incomesStep.isCompleted
            ),
            actual = completionsDataSource.setState.value
        )
        assertEquals(
            expected = RecurrenceBuilderLists(
                fixedExpensesList = expected.fixedExpensesStep.list,
                variableExpensesList = expected.variableExpensesStep.list,
                seasonalExpensesList = expected.seasonalExpensesStep.list,
                incomesList = expected.incomesStep.list
            ),
            actual = listsDataSource.setState.value
        )
    }
}
