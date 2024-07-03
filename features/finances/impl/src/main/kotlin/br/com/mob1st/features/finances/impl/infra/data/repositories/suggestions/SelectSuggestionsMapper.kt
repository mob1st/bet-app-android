package br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions

import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.features.finances.impl.Category_view
import br.com.mob1st.features.finances.impl.SelectSuggestions
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.infra.data.repositories.categories.SelectCategoryViewsMapper
import br.com.mob1st.features.finances.impl.infra.data.system.StringIdProvider
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import timber.log.Timber

/**
 * Maps a list of [SelectSuggestions] provided by the database to a list of [CategorySuggestion] domain entity.
 * It uses the [SelectCategoryViewsMapper] to map the linked categories. For that it's important to use the same
 * contract in the query used to get the category data.
 * @property selectCategoryViewMapper The mapper for the category data.
 * @property stringIdProvider The provider for the string resources.
 */
internal class SelectSuggestionsMapper(
    private val selectCategoryViewMapper: SelectCategoryViewsMapper,
    private val stringIdProvider: StringIdProvider,
) {
    /**
     * Maps the given [query] to a list of [CategorySuggestion] domain entities.
     * @param type The type of the categories to be mapped.
     * @param query The list of [SelectSuggestions] to be mapped.
     * @return The list of [CategorySuggestion] domain entities.
     * @see SelectCategoryViewsMapper
     */
    fun map(
        type: CategoryType,
        query: List<SelectSuggestions>,
    ): List<CategorySuggestion> {
        return query.groupBy { it.sug_id }.mapNotNull { entry ->
            val first = entry.value.first()
            val name = stringIdProvider[first.sug_name]
            if (name == null) {
                Timber.w("Unknown suggestion name: $this. Suggestion will be discarded from the list.")
                return@mapNotNull null
            }
            val linkedCategory = runCatching {
                selectCategoryViewMapper.map(type, entry)
            }.onFailure {
                Timber.e(it, "Error mapping category view ${first.cat_id} from suggestion ${first.sug_id}.")
                return@mapNotNull null
            }.getOrNull()
            CategorySuggestion(
                id = RowId(first.sug_id),
                nameResId = name,
                linkedCategory = linkedCategory,
            )
        }
    }
}

private fun SelectCategoryViewsMapper.map(
    type: CategoryType,
    entry: Map.Entry<Long, List<SelectSuggestions>>,
): Category? {
    val views = entry.value.mapNotNull {
        it.asCategoryView()
    }
    val categories = map(type, views)
    if (categories.size > 1) {
        // the relation contract between suggestion and category should be 1:1
        // it can be ignored, but it's important to log it.
        Timber.w("More than one category was mapped for a single suggestion.")
    }
    return categories.firstOrNull()
}

private fun SelectSuggestions.asCategoryView(): Category_view? {
    return cat_id?.let { id ->
        check(cat_linked_suggestion_id == sug_id) {
            "The category's linked suggestion id must be the same as the suggestion id."
        }
        // manually creates the SqlDelight generated class to reuse the SelectCategoryViewsMapper method
        Category_view(
            cat_id = id,
            cat_name = checkNotNull(cat_name),
            cat_is_expense = checkNotNull(cat_is_expense),
            cat_amount = checkNotNull(cat_amount),
            cat_created_at = cat_created_at.orEmpty(),
            cat_linked_suggestion_id = cat_linked_suggestion_id,
            frc_day_of_month = frc_day_of_month,
            vrc_day_of_week = vrc_day_of_week,
            src_day = src_day,
            src_month = src_month,
        )
    }
}
