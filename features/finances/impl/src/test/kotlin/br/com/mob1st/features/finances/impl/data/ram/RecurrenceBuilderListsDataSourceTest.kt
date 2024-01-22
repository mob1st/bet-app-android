package br.com.mob1st.features.finances.impl.data.ram

import br.com.mob1st.core.kotlinx.coroutines.DefaultCoroutineDispatcher
import br.com.mob1st.core.kotlinx.structures.generateId
import br.com.mob1st.features.finances.impl.data.system.RecurrenceLocalizationProvider
import br.com.mob1st.features.finances.impl.fakes.FakeRecurrenceLocalizationProvider
import br.com.mob1st.tests.featuresutils.RandomIdGenerator
import io.mockk.every
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(RandomIdGenerator::class)
class RecurrenceBuilderListsDataSourceTest {

    private lateinit var subject: RecurrenceBuilderListsDataSource

    private lateinit var localizationProvider: RecurrenceLocalizationProvider
    private lateinit var dispatcher: TestDispatcher

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        localizationProvider = FakeRecurrenceLocalizationProvider()
        dispatcher = UnconfinedTestDispatcher()
        subject = RecurrenceBuilderListsDataSource(
            localizationProvider = localizationProvider,
            default = DefaultCoroutineDispatcher(dispatcher)
        )
    }

    @Test
    fun `GIVEN a localization provider WHEN get THEN return the list of suggestions as default`() = runTest {
        every { generateId() } returns "a"
        val expected = RecurrenceBuilderLists(
            fixedExpensesList = FixedExpanseFactory(localizationProvider).toPersistentList(),
            variableExpensesList = VariableExpanseFactory(localizationProvider).toPersistentList(),
            seasonalExpensesList = SeasonalExpanseFactory(localizationProvider).toPersistentList(),
            incomesList = IncomeFactory(localizationProvider).toPersistentList()
        )
        val actual = subject.data.first()
        assertEquals(expected, actual)
    }
}
