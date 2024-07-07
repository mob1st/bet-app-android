package br.com.mob1st.features.finances.impl.infra.data.repositories

import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceBuilder
import br.com.mob1st.features.finances.impl.infra.data.morphisms.toData
import br.com.mob1st.features.finances.impl.infra.data.preferences.RecurrenceBuilderCompletions
import br.com.mob1st.features.finances.impl.infra.data.ram.RecurrenceBuilderLists
import br.com.mob1st.tests.featuresutils.FakeUnitaryDataSource
import br.com.mob1st.tests.featuresutils.fixture
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class RecurrencesBuilderRepositoryImplTest {
    private lateinit var subject: RecurrenceBuilderRepositoryImpl

    private lateinit var listsDataSource: FakeUnitaryDataSource<RecurrenceBuilderLists>
    private lateinit var completionsDataSource: FakeUnitaryDataSource<RecurrenceBuilderCompletions>

    @BeforeEach
    fun setUp() {
        listsDataSource =
            FakeUnitaryDataSource(
                MutableStateFlow(fixture()),
            )
        completionsDataSource =
            FakeUnitaryDataSource(
                MutableStateFlow(fixture()),
            )
        subject =
            RecurrenceBuilderRepositoryImpl(
                listsDataSource = listsDataSource,
                completionsDataSource = completionsDataSource,
                io = IoCoroutineDispatcher(UnconfinedTestDispatcher()),
            )
    }

    @Test
    fun `GIVEN the lists of recurrences and their completion status THEN set the lists of each step`() =
        runTest {
            val fixture = fixture<RecurrenceBuilder>()
            val expected = fixture.toData()
            subject.set(fixture)
            assertEquals(
                expected = expected.completions,
                actual = completionsDataSource.setState.value,
            )
            assertEquals(
                expected = expected.lists,
                actual = listsDataSource.setState.value,
            )
        }
}
