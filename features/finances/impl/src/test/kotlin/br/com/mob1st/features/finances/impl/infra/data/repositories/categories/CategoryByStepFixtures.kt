package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

import br.com.mob1st.core.database.Categories
import br.com.mob1st.features.finances.impl.utils.moduleFixture

internal object CategoryByStepFixtures {
    /**
     * Generates a random list of categories mixing expenses and not expenses.
     * It's useful for testing the select of categories by step, where the filter depend on this property + the recurrence.
     * It generates 4 categories, providing 2 categories per step.
     * The recurrences columns should be generate through the [recurrenceBuilder] block.
     * @param recurrenceBuilder a lambda that returns a [RecurrenceColumns] instance.
     * @return a list of categories.
     */
    fun generate(
        recurrenceBuilder: () -> RecurrenceColumns,
    ): MutableList<Categories> {
        val categories = mutableListOf<Categories>()
        val booleans = listOf(true, false, true, false)
        for (isExpense in booleans) {
            val recurrenceColumns = recurrenceBuilder()
            val category = moduleFixture<Categories>().copy(
                recurrences = recurrenceColumns.rawRecurrences,
                recurrence_type = recurrenceColumns.rawType,
                is_expense = isExpense,
            )
            categories.add(category)
        }
        return categories
    }
}
