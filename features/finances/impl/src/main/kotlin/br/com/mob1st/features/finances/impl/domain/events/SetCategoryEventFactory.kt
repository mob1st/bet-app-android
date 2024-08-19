package br.com.mob1st.features.finances.impl.domain.events

import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences

/**
 * Factory to create the event that tracks the edits/creates of a category.
 */
internal fun interface SetCategoryEventFactory {
    /**
     * Creates the event that tracks the edits/creates of a category.
     * @param category The category that was edited/created.
     * @return The event to be tracked.
     */
    fun create(
        category: Category,
    ): AnalyticsEvent

    companion object : SetCategoryEventFactory {
        override fun create(category: Category): AnalyticsEvent {
            return AnalyticsEvent.categorySent(category)
        }
    }
}

private fun AnalyticsEvent.Companion.categorySent(
    category: Category,
) = AnalyticsEvent(
    name = "category_sent",
    params = mapOf(
        "name" to category.name,
        "amount" to category.amount.cents,
        "is_expense" to category.isExpense,
        "is_suggested" to category.isSuggested,
        "create" to !category.id.isWritten(),
        category.recurrences.keyValue(),
    ),
)

private fun Recurrences.keyValue() = when (this) {
    is Recurrences.Fixed -> "fixed" to day.value
    Recurrences.Variable -> "variable" to "weekly"
    is Recurrences.Seasonal -> "seasonal" to daysOfYear.joinToString(",") { it.value.toString() }
}
