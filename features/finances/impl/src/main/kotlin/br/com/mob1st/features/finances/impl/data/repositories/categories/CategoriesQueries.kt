package br.com.mob1st.features.finances.impl.data.repositories.categories

import app.cash.sqldelight.TransactionCallbacks
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.features.finances.impl.CategoriesQueries
import br.com.mob1st.features.finances.impl.Category_view
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

internal fun CategoriesQueries.selectManuallyCreatedCategories(
    context: CoroutineContext,
    isExpense: Boolean,
    mapper: (query: List<Category_view>) -> List<Category>,
): Flow<List<Category>> {
    return selectManuallyCreatedCategories(isExpense)
        .asFlow()
        .mapToList(context)
        .map(mapper)
}

context(TransactionCallbacks)
internal fun CategoriesQueries.insertCategories(
    category: Category,
    linkedSuggestion: CategorySuggestion?,
) {
    insertCategory(
        name = category.name,
        amount = category.amount.cents,
        linked_suggestion_id = linkedSuggestion?.id?.value,
        is_expense = category.isExpense,
    )
}

context(TransactionCallbacks)
internal fun CategoriesQueries.insertRecurrences(
    id: RowId,
    recurrences: Recurrences,
) {
    when (recurrences) {
        is Recurrences.Fixed -> insertFixedRecurrence(id, recurrences)
        is Recurrences.Variable -> insertVariableRecurrence(id, recurrences)
        is Recurrences.Seasonal -> insertSeasonalRecurrence(id, recurrences)
    }
}

private fun CategoriesQueries.insertFixedRecurrence(
    id: RowId,
    recurrences: Recurrences.Fixed,
) {
    recurrences.daysOfMonth.forEach { day ->
        insertFixedRecurrence(
            id = id.value,
            day_of_month = day.value,
        )
    }
}

private fun CategoriesQueries.insertVariableRecurrence(
    id: RowId,
    recurrences: Recurrences.Variable,
) {
    recurrences.daysOfWeek.forEach { day ->
        insertVariableRecurrence(
            id = id.value,
            day_of_week = day.value,
        )
    }
}

private fun CategoriesQueries.insertSeasonalRecurrence(
    id: RowId,
    recurrences: Recurrences.Seasonal,
) {
    recurrences.daysAndMonths.forEach { (day, month) ->
        insertSeasonalRecurrence(
            id = id.value,
            day = day.value,
            month = month.value,
        )
    }
}

internal fun CategoriesQueries.update(category: Category) {
    updateCategory(
        id = category.id.value,
        name = category.name,
        amount = category.amount.cents,
    )
}

internal fun CategoriesQueries.delete(
    category: Category,
) {
    deleteCategory(category.id.value)
}

internal fun CategoriesQueries.deleteRecurrence(
    category: Category,
    recurrenceIndex: Int,
) {
    val (p1: Int, p2: Int) = when (val recurrences = category.recurrences) {
        is Recurrences.Fixed -> recurrences.daysOfMonth[recurrenceIndex].value to 0
        is Recurrences.Variable -> recurrences.daysOfWeek[recurrenceIndex].value to 0
        is Recurrences.Seasonal -> {
            val dayAndMonth = recurrences.daysAndMonths[recurrenceIndex]
            dayAndMonth.day.value to dayAndMonth.month.value
        }
    }
    deleteRecurrence(
        id = category.id.value,
        p1 = p1,
        p2 = p2,
    )
}
