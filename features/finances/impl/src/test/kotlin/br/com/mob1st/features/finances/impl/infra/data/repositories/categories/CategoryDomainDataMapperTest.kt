package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.Category_view
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfYear
import br.com.mob1st.features.finances.impl.domain.values.Month
import br.com.mob1st.features.finances.impl.utils.moduleFixture
import com.appmattus.kotlinfixture.Fixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class CategoryDomainDataMapperTest {
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
        val actual = CategoryDomainDataMapper.map(data)
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
        val actual = CategoryDomainDataMapper.map(listOf(categoryView))
        assertEquals(
            Recurrences.Fixed(dayOfMonth),
            actual[0].recurrences,
        )
    }

    @Test
    fun `GIVEN a category views And no recurrence is filled WHEN map THEN assert it's mapped as variable recurrence`() {
        val categoryView = fixture<Category_view>().copy(
            frc_day_of_month = null,
            src_day = null,
            src_month = null,
        )
        val actual = CategoryDomainDataMapper.map(listOf(categoryView))
        assertEquals(
            Recurrences.Variable,
            actual[0].recurrences,
        )
    }

    @Test
    fun `GIVEN a list of categories views And day and month are filled WHEN map THEN assert it's mapped as seasonal recurrence`() {
        val (day, month) = fixture<DayOfYear>()
        val categoryView = fixture<Category_view>().copy(
            frc_day_of_month = null,
            src_day = day.value,
            src_month = month.value,
        )
        val actual = CategoryDomainDataMapper.map(listOf(categoryView))
        assertEquals(
            Recurrences.Seasonal(listOf(DayOfYear(day, month))),
            actual[0].recurrences,
        )
    }

    @Test
    fun `GIVEN a category view And the first day-month are filled but the others don't WHEN map And category type is not THEN assert error is thrown`() {
        val firstCategoryView = fixture<Category_view>().copy(
            cat_id = 1,
            frc_day_of_month = null,
            src_day = 1,
            src_month = 1,
        )
        val secondCategoryView = fixture<Category_view>().copy(
            cat_id = 1,
            frc_day_of_month = null,
            src_day = null,
            src_month = null,
        )
        assertThrows<NullPointerException> {
            CategoryDomainDataMapper.map(listOf(firstCategoryView, secondCategoryView))
        }
    }

    @Test
    fun `GIVEN a category WHEN map THEN assert category is mapped`() {
        val name = fixture<String>()
        val isExpense = fixture<Boolean>()
        val amount = fixture<Money>()
        val id = fixture<Category.Id>()
        val categoryView = fixture<Category_view>().copy(
            cat_id = id.value,
            cat_name = name,
            cat_is_expense = isExpense,
            cat_amount = amount.cents,
        )
        val anyRecurrences = fixture<Recurrences>()
        val actual = CategoryDomainDataMapper.map(
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
