package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

import br.com.mob1st.core.database.Categories
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.Category

/**
 * Maps a [Categories] entity to a [Category] domain entity.
 */
internal interface CategoriesDataMap : (Categories) -> Category {
    /**
     * Default implementation of the [CategoriesDataMap] interface.
     */
    companion object : CategoriesDataMap {
        override fun invoke(entity: Categories): Category {
            return entity.toDomain()
        }
    }
}

/**
 * Maps the receiver [Categories] to a [Category] domain entity.
 * @return the [Category] domain entity.
 */
private fun Categories.toDomain(): Category {
    val recurrenceColumns = RecurrenceColumns(
        rawType = recurrence_type,
        rawRecurrences = recurrences,
    )
    return Category(
        id = Category.Id(id),
        name = name,
        image = Uri(image),
        amount = Money(amount),
        isExpense = is_expense,
        isSuggested = is_suggested,
        recurrences = recurrenceColumns.toRecurrences(),
    )
}
