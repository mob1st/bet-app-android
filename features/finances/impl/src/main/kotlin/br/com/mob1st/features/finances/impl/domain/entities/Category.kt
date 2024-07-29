package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Identifiable
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth

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
    val name: String,
    val image: Uri,
    val amount: Money,
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

    /**
     * Creates a new category instance.
     */
    interface Factory {
        /**
         * Creates a new category from the given [suggestion].
         * It's a useful for the user to create a category from a predefined set of suggestions.
         * @param step The step in the budget builder where the category is being created. It's useful to understand if
         * the category is an expense or an income, and also to define its default recurrences.
         * @param suggestion The suggestion to create the category from.
         * @return The created category.
         */
        fun create(
            step: BuilderNextAction.Step,
            suggestion: CategorySuggestion,
        ): Category
    }

    companion object : Factory {
        override fun create(
            step: BuilderNextAction.Step,
            suggestion: CategorySuggestion,
        ): Category {
            return Category(
                name = suggestion.name,
                image = suggestion.image,
                amount = Money.Zero,
                isExpense = step.isExpense,
                recurrences = step.type.toDefaultRecurrences(),
                isSuggested = true,
            )
        }
    }
}

private fun RecurrenceType.toDefaultRecurrences(): Recurrences {
    return when (this) {
        RecurrenceType.Fixed -> Recurrences.Fixed(DayOfMonth(1))
        RecurrenceType.Variable -> Recurrences.Variable
        RecurrenceType.Seasonal -> Recurrences.Seasonal(emptyList())
    }
}
