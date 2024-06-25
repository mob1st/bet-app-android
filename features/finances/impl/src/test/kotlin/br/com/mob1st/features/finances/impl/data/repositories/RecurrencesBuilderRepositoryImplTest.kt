package br.com.mob1st.features.finances.impl.data.repositories

import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.finances.impl.data.morphisms.RecurrenceBuilderCache
import br.com.mob1st.features.finances.impl.data.morphisms.toData
import br.com.mob1st.features.finances.impl.data.morphisms.toDomain
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
    fun `GIVEN the lists of recurrences and their completion status WHEN get builder THEN combine all of them`() =
        runTest {
            val actual = subject.get().first()
            val cache =
                RecurrenceBuilderCache(
                    lists = listsDataSource.setState.value!!,
                    completions = completionsDataSource.setState.value!!,
                )
            val expected = cache.toDomain()
            assertEquals(expected, actual)
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
