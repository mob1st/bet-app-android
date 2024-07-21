package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

import br.com.mob1st.core.database.Categories
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.CategoriesQueries
import br.com.mob1st.features.finances.impl.domain.entities.Category

/**
 * Maps the receiver [Categories] to a [Category] domain entity.
 * @return the [Category] domain entity.
 */
internal fun Categories.toDomain(): Category {
    val columns = RecurrenceColumns(
        rawType = recurrence_type,
        rawRecurrences = recurrences,
    )
    return Category(
        id = Category.Id(id),
        name = name,
        image = Uri(image),
        amount = Money(amount),
        isExpense = is_expense,
        suggested = suggested,
        recurrences = columns.toRecurrences(),
    )
}

/**
 * Inserts the given [category] into the database.
 * It must be called inside a transaction created by the caller of this function.
 * @param category the category to be inserted.
 */
internal fun CategoriesQueries.insert(category: Category) {
    val columns = RecurrenceColumns.from(category.recurrences)
    insertCategory(
        name = category.name,
        amount = category.amount.cents,
        image = category.image.value,
        is_expense = category.isExpense,
        suggested = category.suggested,
        recurrences = columns.rawRecurrences,
        recurrence_type = columns.rawType,
    )
}

/**
 * Updates the given [category] in the database.
 */
internal fun CategoriesQueries.update(category: Category) {
    val column = RecurrenceColumns.from(category.recurrences)
    updateCategory(
        id = category.id.value,
        name = category.name,
        amount = category.amount.cents,
        image = category.image.value,
        recurrences = column.rawRecurrences,
    )
}
