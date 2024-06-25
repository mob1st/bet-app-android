package br.com.mob1st.features.finances.impl.data.morphisms

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.features.finances.impl.Category_view
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayAndMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfWeek
import br.com.mob1st.features.finances.impl.domain.values.Month
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType

/**
 * Maps the
 */
object ListCategoryViewMapper {
    fun map(type: CategoryType, query: List<Category_view>): List<Category> {
        return query.groupBy { it.cat_id }.map { entry ->
            entry.toDomain(type)
        }
    }
}

private fun Map.Entry<Long, List<Category_view>>.toDomain(
    type: CategoryType,
): Category {
    val categoryProjection = value.first()
    return Category(
        id = RowId(categoryProjection.cat_id),
        name = categoryProjection.cat_name,
        amount = Money(categoryProjection.cat_amount),
        isExpense = categoryProjection.cat_is_expense,
        recurrences = value.toRecurrences(type),
    )
}

private fun List<Category_view>.toRecurrences(
    type: CategoryType,
): Recurrences {
    return when (type) {
        CategoryType.Fixed -> toFixedRecurrence()
        CategoryType.Variable -> toVariableRecurrence()
        CategoryType.Seasonal -> toSeasonalRecurrences()
    }
}

private fun List<Category_view>.toFixedRecurrence(): Recurrences.Fixed {
    val daysOfMonth = map { projection -> DayOfMonth(checkNotNull(projection.day_of_month)) }
    return Recurrences.Fixed(daysOfMonth)
}

private fun List<Category_view>.toVariableRecurrence(): Recurrences.Variable {
    val daysOfWeek = map { projection -> DayOfWeek(checkNotNull(projection.day_of_week)) }
    return Recurrences.Variable(daysOfWeek)
}

private fun List<Category_view>.toSeasonalRecurrences(): Recurrences.Seasonal {
    val daysAndMonths = map { projection ->
        DayAndMonth(
            day = DayOfMonth(checkNotNull(projection.day)),
            month = Month(checkNotNull(projection.month)),
        )
    }
    return Recurrences.Seasonal(daysAndMonths)
}
