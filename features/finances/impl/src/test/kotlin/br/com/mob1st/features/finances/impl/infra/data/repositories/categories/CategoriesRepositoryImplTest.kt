package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.features.finances.impl.Category_view
import br.com.mob1st.features.finances.impl.TwoCentsDb
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayAndMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.Month
import br.com.mob1st.features.finances.impl.utils.moduleFixture
import br.com.mob1st.features.finances.impl.utils.testTwoCentsDb
import com.appmattus.kotlinfixture.Fixture
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class CategoriesRepositoryImplTest {
    private lateinit var repository: CategoriesRepositoryImpl

    private lateinit var db: TwoCentsDb
    private lateinit var selectCategoryViewMapper: SelectCategoryViewsMapper

    private lateinit var fixture: Fixture

    private val io: IoCoroutineDispatcher = IoCoroutineDispatcher(
        UnconfinedTestDispatcher(),
    )

    @BeforeEach
    fun setUp() {
        fixture = moduleFixture
        db = testTwoCentsDb()
        selectCategoryViewMapper = SelectCategoryViewsMapper
        repository = CategoriesRepositoryImpl(
            io = io,
            db = db,
            selectCategoryViewMapper = selectCategoryViewMapper,
        )
    }

    @Test
    fun `GIVEN a category list WHEN get manually created THEN verify mapping is done`() = runTest(io) {
        db.categoriesQueries.insertCategory(
            name = "category",
            amount = 100,
            is_expense = true,
            linked_suggestion_id = null,
        )
        val id = db.commonsQueries.lastInsertRowId().executeAsOne()
        db.categoriesQueries.insertFixedRecurrence(id, 1)
        val actual = repository.getManuallyCreatedBy(FixedExpensesStep).first().first()
        val expected = Category(
            id = RowId(id),
            name = "category",
            amount = Money(100),
            isExpense = true,
            recurrences = Recurrences.Fixed(DayOfMonth(1)),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a category WHEN delete THEN assert category is deleted`() = runTest(io) {
        db.categoriesQueries.insertCategory(
            name = "category",
            amount = 100,
            is_expense = true,
            linked_suggestion_id = null,
        )
        val id = db.commonsQueries.lastInsertRowId().executeAsOne()

        val categoryView = db.categoriesQueries.selectCategoryById(id).executeAsOne()
        val category = fixture<Category>().copy(id = RowId(categoryView.cat_id))

        repository.delete(category)

        val actual = db.categoriesQueries.selectCategoryById(id).executeAsOneOrNull()
        assertNull(actual)
    }

    @Test
    fun `GIVEN a category with fixed recurrence WHEN add THEN assert category is inserted`() = runTest(io) {
        val recurrences = Recurrences.Fixed(DayOfMonth(1))
        val category = fixture<Category>().copy(recurrences = recurrences)
        repository.add(category, null)

        val actual = db.categoriesQueries.selectCategoryById(1).executeAsList()
        val expected = listOf(
            Category_view(
                cat_id = 1,
                cat_name = category.name,
                cat_is_expense = category.isExpense,
                cat_linked_suggestion_id = null,
                cat_amount = category.amount.cents,
                cat_created_at = actual.first().cat_created_at,
                frc_day_of_month = 1,
                src_day = null,
                src_month = null,
            ),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a category with variable recurrence WHEN add THEN assert category is inserted`() = runTest(io) {
        val recurrences = Recurrences.Variable
        val category = fixture<Category>().copy(recurrences = recurrences)
        repository.add(category, null)

        val actual = db.categoriesQueries.selectCategoryById(1).executeAsList()
        val expected = listOf(
            Category_view(
                cat_id = 1,
                cat_name = category.name,
                cat_is_expense = category.isExpense,
                cat_linked_suggestion_id = null,
                cat_amount = category.amount.cents,
                cat_created_at = actual.first().cat_created_at,
                frc_day_of_month = null,
                src_day = null,
                src_month = null,
            ),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a category with seasonal recurrence WHEN add THEN assert category is inserted`() = runTest(io) {
        val recurrences = Recurrences.Seasonal(
            listOf(
                DayAndMonth(DayOfMonth(1), Month(1)),
                DayAndMonth(DayOfMonth(1), Month(2)),
            ),
        )
        val category = fixture<Category>().copy(recurrences = recurrences)
        repository.add(category, null)

        val actual = db.categoriesQueries.selectCategoryById(1).executeAsList()
        val expected = listOf(
            Category_view(
                cat_id = 1,
                cat_name = category.name,
                cat_is_expense = category.isExpense,
                cat_linked_suggestion_id = null,
                cat_amount = category.amount.cents,
                cat_created_at = actual.first().cat_created_at,
                frc_day_of_month = null,
                src_day = 1,
                src_month = 1,
            ),
            Category_view(
                cat_id = 1,
                cat_name = category.name,
                cat_is_expense = category.isExpense,
                cat_linked_suggestion_id = null,
                cat_amount = category.amount.cents,
                cat_created_at = actual.last().cat_created_at,
                frc_day_of_month = null,
                src_day = 1,
                src_month = 2,
            ),
        )
        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @ArgumentsSource(InsertLinkedSuggestionArgumentsProvider::class)
    fun `GIVEN a category WHEN add with a linked suggestion THEN verify suggestion is inserted`(
        category: Category,
        type: String,
    ) = runTest(io) {
        val suggestions = db.suggestionsQueries.selectSuggestions(
            is_expense = category.isExpense,
            type = type,
        ).executeAsList()
        val suggestion = suggestions.random()

        repository.add(category, fixture<CategorySuggestion>().copy(id = RowId(suggestion.sug_id)))

        val actual = db.suggestionsQueries.selectSuggestionsByCategory(1).executeAsOne()
        assertEquals(suggestion.sug_id, actual.sug_id)
    }

    @Test
    fun `GIVEN an existing category WHEN set an update THEN assert category is updated`() = runTest(io) {
        db.categoriesQueries.insertCategory(
            name = "category",
            amount = 100,
            is_expense = true,
            linked_suggestion_id = null,
        )
        val id = db.commonsQueries.lastInsertRowId().executeAsOne()
        db.categoriesQueries.insertFixedRecurrence(id, 2)

        val updatedCategory = Category(
            id = RowId(id),
            name = "new category",
            amount = Money(200),
            isExpense = true,
            recurrences = Recurrences.Fixed(DayOfMonth(2)),
        )

        repository.set(updatedCategory)

        val actual = db.categoriesQueries.selectManuallyCreatedCategories(true).executeAsList().first {
            id == it.cat_id
        }
        val expected = Category_view(
            cat_id = id,
            cat_name = "new category",
            cat_is_expense = true,
            cat_linked_suggestion_id = null,
            cat_amount = 200,
            cat_created_at = actual.cat_created_at,
            frc_day_of_month = 2,
            src_day = null,
            src_month = null,
        )
        assertEquals(expected, actual)
    }

    object InsertLinkedSuggestionArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    moduleFixture<Category> {
                        repeatCount { 1 }
                    }.copy(isExpense = true),
                    "fixed",
                ),
                Arguments.of(
                    moduleFixture<Category> {
                        repeatCount { 1 }
                    }.copy(isExpense = true),
                    "variable",
                ),
                Arguments.of(
                    moduleFixture<Category> {
                        repeatCount { 1 }
                    }.copy(isExpense = false),
                    "fixed",
                ),
            )
        }
    }
}
