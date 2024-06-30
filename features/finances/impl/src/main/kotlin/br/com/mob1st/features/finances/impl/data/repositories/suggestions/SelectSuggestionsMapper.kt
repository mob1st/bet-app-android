package br.com.mob1st.features.finances.impl.data.repositories.suggestions

import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.features.finances.impl.Category_view
import br.com.mob1st.features.finances.impl.SelectSuggestions
import br.com.mob1st.features.finances.impl.data.repositories.categories.SelectCategoryViewsMapper
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType

/**
 * Maps a list of [SelectSuggestions] provided by the database to a list of [CategorySuggestion] domain entity.
 * It uses the [SelectCategoryViewsMapper] to map the linked categories. For that it's important to use the same contract
 * in the query used to get the category data.
 * @property listCategoryViewMapper The mapper for the category data.
 */
internal class SelectSuggestionsMapper(
    private val listCategoryViewMapper: SelectCategoryViewsMapper,
) {
    /**
     * Maps the given [query] to a list of [CategorySuggestion] domain entities.
     * @param type The type of the categories to be mapped.
     * @param query The list of [SelectSuggestions] to be mapped.
     * @return The list of [CategorySuggestion] domain entities.
     * @see SelectCategoryViewsMapper
     */
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

private fun SelectCategoryViewsMapper.map(
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
        // manually creates the SqlDelight generated class to reuse the mapper
        Category_view(
            cat_id = cat_id,
            cat_name = sug_name,
            cat_is_expense = checkNotNull(cat_is_expense),
            cat_amount = checkNotNull(cat_amount),
            cat_created_at = checkNotNull(cat_created_at),
            cat_linked_suggestion_id = cat_linked_suggestion_id,
            frc_day_of_month = frc_day_of_month,
            vrc_day_of_week = vrc_day_of_week,
            src_day = src_day,
            src_month = src_month,
        )
    }
}
