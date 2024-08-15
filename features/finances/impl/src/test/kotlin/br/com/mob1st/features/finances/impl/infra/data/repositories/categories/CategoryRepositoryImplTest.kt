package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

import br.com.mob1st.core.data.suspendTransaction
import br.com.mob1st.core.database.Categories
import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.finances.impl.TwoCentsDb
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceType
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.fixtures.category
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfYear
import br.com.mob1st.features.finances.impl.infra.data.fixtures.categories
import br.com.mob1st.features.finances.impl.infra.data.fixtures.recurrenceColumns
import br.com.mob1st.features.finances.impl.utils.testTwoCentsDb
import br.com.mob1st.tests.featuresutils.failOnIndex
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class CategoryRepositoryImplTest {
    private lateinit var repository: CategoryRepositoryImpl
    private lateinit var db: TwoCentsDb
    private lateinit var categoriesDataMap: CategoriesDataMap

    private val io: IoCoroutineDispatcher = IoCoroutineDispatcher(
        UnconfinedTestDispatcher(),
    )

    @BeforeEach
    fun setUp() {
        categoriesDataMap = mockk()
        db = testTwoCentsDb()
        repository = CategoryRepositoryImpl(
            io = io,
            db = db,
            categoriesDataMap = categoriesDataMap,
        )
    }

    @ParameterizedTest
    @MethodSource("getByTypeSource")
    fun `GIVEN a list of categories WHEN get by type And is expense THEN assert filter is done`(
        type: RecurrenceType,
        expectedRawRecurrenceType: String,
    ) = runTest {
        // Given
        val sample = Arb.categories().chunked(10..20).filter { list ->
            list.hasGoodData(expectedRawRecurrenceType)
        }
        val isExpense = Arb.boolean().next()

        insertCategories(sample.next())
        val actual = mutableListOf<Categories>()
        captureQueryResults(actual)
        // When
        repository.getByIsExpenseAndRecurrencesType(
            isExpense = isExpense,
            recurrenceType = type,
        ).take(1).collect()

        // Then
        assertTrue(actual.all { it.is_expense == isExpense })
        assertTrue(actual.all { it.recurrence_type == expectedRawRecurrenceType })
    }

    @ParameterizedTest
    @MethodSource("getByTypeSource")
    fun `GIVEN a list of categories WHEN count by type And is expense THEN assert filter is done`(
        type: RecurrenceType,
        rawRecurrenceType: String,
    ) = runTest {
        // Given
        val isExpense = Arb.boolean().next()
        val otherRecurrenceType = Arb
            .recurrenceColumns()
            .map { it.rawType }
            .filter { it != rawRecurrenceType }
            .next()
        val sample = listOf(
            Arb.categories().map {
                it.copy(
                    is_expense = isExpense,
                    recurrence_type = rawRecurrenceType,
                )
            }.next(),
            Arb.categories().map {
                it.copy(
                    is_expense = !isExpense,
                    recurrence_type = rawRecurrenceType,
                )
            }.next(),
            Arb.categories().map {
                it.copy(
                    is_expense = Arb.boolean().next(),
                    recurrence_type = otherRecurrenceType,
                )
            }.next(),
        )
        insertCategories(sample)
        val actual = repository.countByIsExpenseAndRecurrencesType(
            isExpense = isExpense,
            recurrenceType = type,
        ).first()
        assertEquals(1, actual)
    }

    @Test
    fun `GIVEN a category WHEN get by id THEN assert filter is done`() = runTest {
        // Given
        val sample = Arb.categories().next()
        insertCategories(listOf(sample))
        val lastInsertedId = db.commonsQueries.lastInsertRowId().executeAsOne()
        val actual = mutableListOf<Categories>()
        captureQueryResults(actual)

        // When
        repository.getById(Category.Id(lastInsertedId)).take(1).collect()

        // Then
        assertEquals(
            lastInsertedId,
            actual.first().id,
        )
    }

    @ParameterizedTest
    @MethodSource("setSource")
    fun `GIVEN two categories WHEN set data of one THEN only this is updated`(
        initialRecurrence: RecurrenceColumns,
        recurrencesToUpdate: Recurrences,
        expectedRecurrences: String?,
    ) = runTest {
        // Given
        val categoriesBeforeUpdate1 = Arb.categories().next()
        val categoriesBeforeUpdate2 = Arb.categories().next().copy(
            recurrence_type = initialRecurrence.rawType,
            recurrences = initialRecurrence.rawRecurrences,
        )

        val (firstCategoryId, secondCategoryId) = insertCategories(
            listOf(categoriesBeforeUpdate1, categoriesBeforeUpdate2),
        )

        val updatedCategory = Arb.category().next().copy(
            id = Category.Id(secondCategoryId),
            recurrences = recurrencesToUpdate,
        )

        // When
        repository.set(updatedCategory)

        // Then
        val categoriesAfterUpdate1 = db.categoriesQueries.selectCategoryById(firstCategoryId).executeAsOne()
        val categoriesAfterUpdate2 = db.categoriesQueries.selectCategoryById(secondCategoryId).executeAsOne()
        assertEquals(
            categoriesBeforeUpdate1.copy(
                id = firstCategoryId,
                created_at = categoriesAfterUpdate1.created_at,
                updated_at = categoriesAfterUpdate1.updated_at,
            ),
            categoriesAfterUpdate1,
        )
        assertEquals(
            categoriesBeforeUpdate2.copy(
                id = secondCategoryId,
                name = updatedCategory.name,
                amount = updatedCategory.amount.cents,
                image = updatedCategory.image.value,
                recurrences = expectedRecurrences,
                created_at = categoriesAfterUpdate2.created_at,
                updated_at = categoriesAfterUpdate2.updated_at,
            ),
            categoriesAfterUpdate2,
        )
        assertNotNull(categoriesAfterUpdate2.updated_at)
    }

    @Test
    fun `GIVEN a category WHEN remove THEN assert only this category is removed`() = runTest {
        val sample = Arb.categories().chunked(2..5).next()
        val ids = insertCategories(sample)
        val picked = ids.random()
        val category = Arb.category().next().copy(
            id = Category.Id(picked),
        )
        repository.remove(category)
        assertNull(
            db.categoriesQueries.selectCategoryById(picked).executeAsOneOrNull(),
        )
        ids.remove(picked)
        ids.forEach { id ->
            assertNotNull(
                db.categoriesQueries.selectCategoryById(id).executeAsOneOrNull(),
            )
        }
    }

    @ParameterizedTest
    @MethodSource("addSource")
    fun `GIVEN an empty database WHEN add THEN assert data is inserted`(
        recurrencesToInsert: Recurrences,
        expectedRecurrences: RecurrenceColumns,
    ) = runTest {
        val sample = Arb.category().next().copy(
            recurrences = recurrencesToInsert,
        )
        repository.add(sample)
        val id = db.commonsQueries.lastInsertRowId().executeAsOne()
        val actual = db.categoriesQueries.selectCategoryById(id).executeAsOne()
        assertEquals(
            Categories(
                id = id,
                name = sample.name,
                amount = sample.amount.cents,
                image = sample.image.value,
                is_expense = sample.isExpense,
                is_suggested = sample.isSuggested,
                recurrences = expectedRecurrences.rawRecurrences,
                recurrence_type = expectedRecurrences.rawType,
                created_at = actual.created_at,
                updated_at = null,
            ),
            actual,
        )
    }

    @Test
    fun `GIVEN a list that fails in the last interaction WHEN add all THEN assert a single transaction is used`() = runTest {
        val categories = Arb
            .category()
            .chunked(6..10)
            .next()
            .failOnIndex(4)
        runCatching {
            repository.addAll(categories)
        }
        assertEquals(
            0,
            db.categoriesQueries.countCategories().executeAsOne(),
        )
    }

    @ParameterizedTest
    @MethodSource("addAllSource")
    fun `GIVEN a list of categories WHEN add all THEN assert data is inserted`(
        recurrencesToInsert: List<Pair<Recurrences, RecurrenceColumns>>,
    ) = runTest {
        val categories = recurrencesToInsert.map {
            Arb.category().next().copy(
                recurrences = it.first,
            )
        }
        repository.addAll(categories)
        val actual = db.categoriesQueries.selectAllCategories().executeAsList()
        assertEquals(
            categories.size,
            actual.size,
        )
        categories.forEachIndexed { index, category ->
            assertEquals(
                Categories(
                    id = actual[index].id,
                    name = category.name,
                    amount = category.amount.cents,
                    image = category.image.value,
                    is_expense = category.isExpense,
                    is_suggested = category.isSuggested,
                    recurrences = recurrencesToInsert[index].second.rawRecurrences,
                    recurrence_type = recurrencesToInsert[index].second.rawType,
                    created_at = actual[index].created_at,
                    updated_at = null,
                ),
                actual[index],
            )
        }
    }

    private suspend fun TestScope.insertCategories(
        list: List<Categories>,
    ): MutableList<Long> {
        val ids = mutableListOf<Long>()
        db.suspendTransaction(coroutineContext) {
            list.forEach {
                db.categoriesQueries.insertCategory(
                    name = it.name,
                    amount = it.amount,
                    image = it.image,
                    is_expense = it.is_expense,
                    is_suggested = it.is_suggested,
                    recurrences = it.recurrences,
                    recurrence_type = it.recurrence_type,
                )
                ids.add(db.commonsQueries.lastInsertRowId().executeAsOne())
            }
        }
        return ids
    }

    private fun captureQueryResults(
        queryResult: MutableList<Categories>,
    ) {
        every { categoriesDataMap(capture(queryResult)) } returns Arb.category().next()
    }

    private fun List<Categories>.hasGoodData(
        recurrenceType: String,
    ): Boolean {
        val hasType = any { it.recurrence_type == recurrenceType }
        val hasNotType = any { it.recurrence_type != recurrenceType }
        val hasExpense = any { it.is_expense }
        val hasNotExpense = any { !it.is_expense }
        return hasType && hasNotType && hasExpense && hasNotExpense
    }

    companion object {
        @JvmStatic
        fun getByTypeSource() = listOf(
            arguments(
                RecurrenceType.Fixed,
                "fixed",
            ),
            arguments(
                RecurrenceType.Variable,
                "variable",
            ),
            arguments(
                RecurrenceType.Seasonal,
                "seasonal",
            ),
        )

        @JvmStatic
        fun setSource() = listOf(
            arguments(
                RecurrenceColumns("fixed", "01"),
                Recurrences.Fixed(DayOfMonth(2)),
                "02",
            ),
            arguments(
                RecurrenceColumns("seasonal", "001,002,003"),
                Recurrences.Seasonal(
                    listOf(
                        DayOfYear(4),
                        DayOfYear(5),
                        DayOfYear(6),
                    ),
                ),
                "004,005,006",
            ),
            arguments(
                RecurrenceColumns("variable", null),
                Recurrences.Variable,
                null,
            ),
        )

        @JvmStatic
        fun addSource() = listOf(
            arguments(
                Recurrences.Fixed(DayOfMonth(1)),
                RecurrenceColumns("fixed", "01"),
            ),
            arguments(
                Recurrences.Seasonal(
                    listOf(
                        DayOfYear(1),
                        DayOfYear(3),
                    ),
                ),
                RecurrenceColumns("seasonal", "001,003"),
            ),
            arguments(
                Recurrences.Variable,
                RecurrenceColumns("variable", null),
            ),
        )

        @JvmStatic
        fun addAllSource() = listOf(
            arguments(
                listOf(
                    Recurrences.Fixed(DayOfMonth(1)) to RecurrenceColumns("fixed", "01"),
                    Recurrences.Fixed(DayOfMonth(2)) to RecurrenceColumns("fixed", "02"),
                ),
            ),
            arguments(
                listOf(
                    Recurrences.Seasonal(
                        listOf(
                            DayOfYear(1),
                            DayOfYear(3),
                        ),
                    ) to RecurrenceColumns("seasonal", "001,003"),
                    Recurrences.Seasonal(
                        listOf(
                            DayOfYear(2),
                            DayOfYear(4),
                        ),
                    ) to RecurrenceColumns("seasonal", "002,004"),
                ),
            ),
            arguments(
                listOf(
                    Recurrences.Variable to RecurrenceColumns("variable", null),
                    Recurrences.Variable to RecurrenceColumns("variable", null),
                ),
            ),
        )
    }
}
