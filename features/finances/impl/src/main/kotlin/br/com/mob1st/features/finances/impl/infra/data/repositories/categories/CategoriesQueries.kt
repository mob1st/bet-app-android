package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

import app.cash.sqldelight.TransactionCallbacks
import br.com.mob1st.features.finances.impl.CategoriesQueries
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences

/**
 * Inserts a new category.
 * This method needs to be called inside a transaction because a category should be inserted with its recurrences.
 * @param category The category to be added.
 * @param linkedSuggestion The suggestion that was linked to the category, if any.
 * @see CategoriesQueries.insertRecurrences to check how the recurrences are inserted.
 */
context(TransactionCallbacks)
internal fun CategoriesQueries.insertCategories(
    category: Category,
    linkedSuggestion: CategorySuggestion?,
) {
    insertCategory(
        name = category.name,
        amount = category.amount.cents,
        linked_suggestion_id = linkedSuggestion?.id?.value,
        image = category.image.value,
        is_expense = category.isExpense,
    )
}

/**
 * Inserts the recurrences of a category.
 * This method have to be called inside a transaction because the recurrences should be inserted only if a category is
 * already inserted.
 * @param id The id of the category that the recurrences are related to.
 * @param recurrences The recurrences to be inserted.
 * @see CategoriesQueries.insertCategories to check how the category is inserted.
 */
context(TransactionCallbacks)
internal fun CategoriesQueries.insertRecurrences(
    id: Long,
    recurrences: Recurrences,
) {
    when (recurrences) {
        is Recurrences.Fixed -> insertFixedRecurrence(id, recurrences)
        is Recurrences.Seasonal -> insertSeasonalRecurrence(id, recurrences)
        is Recurrences.Variable -> {}
    }
}

private fun CategoriesQueries.insertFixedRecurrence(
    id: Long,
    recurrences: Recurrences.Fixed,
) {
    insertFixedRecurrence(
        id = id,
        day_of_month = recurrences.day.value,
    )
}

private fun CategoriesQueries.insertSeasonalRecurrence(
    id: Long,
    recurrences: Recurrences.Seasonal,
) {
    recurrences.daysAndMonths.forEach { (day, month) ->
        insertSeasonalRecurrence(
            id = id,
            day = day.value,
            month = month.value,
        )
    }
}
