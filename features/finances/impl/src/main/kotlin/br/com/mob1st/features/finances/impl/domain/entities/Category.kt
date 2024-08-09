package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Identifiable
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.core.kotlinx.structures.Uri

/**
 * Represents a category of expenses or incomes.
 * @property id The unique identifier of the category.
 * @property name The name of the category.
 * @property image The image of the category.
 * @property amount The amount of the category.
 * @property isExpense Indicates if the category is an expense or an income.
 * @property recurrences The recurrences of the category, which means how often it happens.
 */
data class Category(
    override val id: Id = Id(),
    val amount: Money = Money.Zero,
    val name: String,
    val image: Uri,
    val isExpense: Boolean,
    val recurrences: Recurrences,
    val isSuggested: Boolean,
) : Identifiable<Category.Id> {
    /**
     * The unique identifier of the category.
     * Zero is the dafult value and an invalid ID, which means that this instance was created in the memory only but
     * it wasn't persisted yet.
     * @property value The value of the identifier.
     */
    @JvmInline
    value class Id(override val value: Long = 0) : RowId
}
