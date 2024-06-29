package br.com.mob1st.features.finances.impl.data.repositories.categories

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.features.finances.impl.Category_view
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayAndMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfWeek
import br.com.mob1st.features.finances.impl.domain.values.Month
import br.com.mob1st.features.finances.impl.fixtures.moduleFixture
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import com.appmattus.kotlinfixture.Fixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class CategoryDataMapperTest {
    private lateinit var fixture: Fixture

    @BeforeEach
    fun setup() {
        fixture = moduleFixture.new {
            factory<Category_view> {
                Category_view(
                    cat_id = moduleFixture(),
                    cat_name = moduleFixture(),
                    cat_is_expense = moduleFixture(),
                    cat_amount = moduleFixture<Money>().cents,
                    cat_linked_suggestion_id = moduleFixture(),
                    cat_created_at = moduleFixture(),
                    frc_day_of_month = moduleFixture<DayOfMonth>().value,
                    vrc_day_of_week = moduleFixture<DayOfWeek>().value,
                    src_month = moduleFixture<Month>().value,
                    src_day = moduleFixture<DayOfMonth>().value,
                )
            }
        }
    }

    @Test
    fun `GIVEN a 5 categories in DB And only two different IDs WHEN map THEN assert it's grouped by id`() {
        val data = listOf(
            fixture<Category_view>().copy(cat_id = 1),
            fixture<Category_view>().copy(cat_id = 1),
            fixture<Category_view>().copy(cat_id = 1),
            fixture<Category_view>().copy(cat_id = 2),
            fixture<Category_view>().copy(cat_id = 2),
        )
        val actual = CategoryDataMapper.map(fixture(), data)
        assertEquals(
            1L,
            actual.first().id.value,
        )
        assertEquals(
            2L,
            actual.last().id.value,
        )
        assertEquals(2, actual.size)
    }

    @Test
    fun `GIVEN a category views And day of month is filled WHEN map THEN assert it's mapped as fixed recurrence`() {
        val dayOfMonth = fixture<DayOfMonth>()
        val categoryView = fixture<Category_view>().copy(
            frc_day_of_month = dayOfMonth.value,
        )
        val actual = CategoryDataMapper.map(CategoryType.Fixed, listOf(categoryView))
        assertEquals(
            Recurrences.Fixed(listOf(dayOfMonth)),
            actual[0].recurrences,
        )
    }

    @Test
    fun `GIVEN a category view And a day of month is null WHEN map And category type is not THEN assert error is thrown`() {
        val categoryView = fixture<Category_view>().copy(
            frc_day_of_month = null,
        )
        assertThrows<IllegalStateException> {
            CategoryDataMapper.map(CategoryType.Fixed, listOf(categoryView))
        }
    }

    @Test
    fun `GIVEN a category views And day of week is filled WHEN map THEN assert it's mapped as variable recurrence`() {
        val dayOfWeek = fixture<DayOfWeek>()
        val categoryView = fixture<Category_view>().copy(
            vrc_day_of_week = dayOfWeek.value,
        )
        val actual = CategoryDataMapper.map(CategoryType.Variable, listOf(categoryView))
        assertEquals(
            Recurrences.Variable(listOf(dayOfWeek)),
            actual[0].recurrences,
        )
    }

    @Test
    fun `GIVEN a category view And day of week is null WHEN map And category type is not THEN assert error is thrown`() {
        val categoryView = fixture<Category_view>().copy(
            vrc_day_of_week = null,
        )
        assertThrows<IllegalStateException> {
            CategoryDataMapper.map(CategoryType.Variable, listOf(categoryView))
        }
    }

    @Test
    fun `GIVEN a list of categories views And day and month are filled WHEN map THEN assert it's mapped as seasonal recurrence`() {
        val (day, month) = fixture<DayAndMonth>()
        val categoryView = fixture<Category_view>().copy(
            src_day = day.value,
            src_month = month.value,
        )
        val actual = CategoryDataMapper.map(CategoryType.Seasonal, listOf(categoryView))
        assertEquals(
            Recurrences.Seasonal(listOf(DayAndMonth(day, month))),
            actual[0].recurrences,
        )
    }

    @Test
    fun `GIVEN a category view And day and month are null WHEN map And category type is not THEN assert error is thrown`() {
        val categoryView = fixture<Category_view>().copy(
            src_day = null,
            src_month = null,
        )
        assertThrows<IllegalStateException> {
            CategoryDataMapper.map(CategoryType.Seasonal, listOf(categoryView))
        }
    }

    @Test
    fun `GIVEN a category WHEN map THEN assert category is mapped`() {
        val name = fixture<String>()
        val isExpense = fixture<Boolean>()
        val amount = fixture<Money>()
        val id = fixture<RowId>()
        val categoryView = fixture<Category_view>().copy(
            cat_id = id.value,
            cat_name = name,
            cat_is_expense = isExpense,
            cat_amount = amount.cents,
        )
        val anyRecurrences = fixture<Recurrences>()
        val actual = CategoryDataMapper.map(
            CategoryType.entries.random(),
            listOf(categoryView),
        ).first().copy(
            // exclude the recurrence from comparison because it's not relevant for this test
            recurrences = anyRecurrences,
        )
        val expected = Category(
            id = id,
            name = name,
            isExpense = isExpense,
            amount = amount,
            recurrences = anyRecurrences,
        )

        assertEquals(expected, actual)
    }
}
