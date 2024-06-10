package br.com.mob1st.features.twocents.builder.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Id
import br.com.mob1st.core.kotlinx.structures.Identifiable
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import kotlinx.collections.immutable.PersistentList

/**
 * Builds the categories for the user.
 * The [recurrenceType] field indicates the type of recurrence of the builder.
 * @property recurrenceType The type of recurrence of the builder.
 */
internal data class Builder(
    val recurrenceType: CategoryType,
    val manuallyAdded: PersistentList<CategoryEntry<String>>,
    val suggested: PersistentList<CategoryEntry<CategorySuggestion>>,
)

/**
 * The entry data used to create categories for the user.
 * It can be a suggested category or a manually added category.
 * @property value The value of the category.
 * @property name The name of the category.
 */
internal data class CategoryEntry<T>(
    override val id: Id = Id(),
    val value: Money,
    val name: T,
) : Identifiable

/**
 * The category suggestion.
 */
internal data class CategorySuggestion(
    override val id: Id,
    val name: Name,
) : Identifiable {
    enum class Name {
        RENT,
        PARTY,
    }
}
