package br.com.mob1st.features.finances.impl.data.morphisms

import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.features.finances.impl.Category_view
import br.com.mob1st.features.finances.impl.SelectSuggestions
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType

internal class SuggestionDataMapper(
    private val listCategoryViewMapper: ListCategoryViewMapper,
) {
    fun map(type: CategoryType, query: List<SelectSuggestions>): List<CategorySuggestion> {
        return query.groupBy { it.sug_id }.mapNotNull { entry ->
            val first = entry.value.first()
            val name = first.sug_name.asSuggestionName() ?: return@mapNotNull null
            CategorySuggestion(
                id = RowId(first.sug_id),
                name = name,
                linkedCategory = listCategoryViewMapper.map(type, entry),
            )
        }
    }
}

private fun String.asSuggestionName(): CategorySuggestion.Name? {
    return when (this) {
        "rent" -> CategorySuggestion.Name.Rent
        else -> null
    }
}

private fun ListCategoryViewMapper.map(
    type: CategoryType,
    entry: Map.Entry<Long, List<SelectSuggestions>>,
): Category? {
    val views = entry.value.mapNotNull { it.asCategoryView() }
    // the firstOrNull only works if the contract of the relation between suggestion and category keep 1x1.
    // if it changes this logic also might change
    return map(type, views).firstOrNull()
}

private fun SelectSuggestions.asCategoryView(): Category_view? {
    return cat_id?.let {
        check(cat_linked_suggestion_id == sug_id)
        Category_view(
            cat_id = cat_id,
            cat_name = sug_name,
            cat_is_expense = checkNotNull(cat_is_expense),
            cat_amount = checkNotNull(cat_amount),
            cat_created_at = checkNotNull(cat_created_at),
            cat_linked_suggestion_id = cat_linked_suggestion_id,
            day_of_month = day_of_month,
            day_of_week = day_of_week,
            day = day,
            month = month,
        )
    }
}
