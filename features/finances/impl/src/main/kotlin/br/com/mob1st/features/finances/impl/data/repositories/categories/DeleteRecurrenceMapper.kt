package br.com.mob1st.features.finances.impl.data.repositories.categories

import br.com.mob1st.features.finances.impl.domain.entities.Recurrences

/**
 * Converts a [Recurrences] item to a pair of integers that can be used to identify which recurrence to delete.
 */
object DeleteRecurrenceMapper {
    /**
     * Maps an list item from a [Recurrences] using the [recurrenceIndex] as reference to get the item, to a pair of
     * integers.
     * The pair of integers will be used to identify which recurrence to delete.
     * If it is a [Recurrences.Fixed] the first integer will be the day of the month and the second will be 0.
     * If it is a [Recurrences.Variable] the first integer will be the day of the week and the second will be 0.
     * If it is a [Recurrences.Seasonal] the first integer will be the day of the month and the second will be the
     * month.
     * @param recurrences The recurrences to be mapped.
     * @param recurrenceIndex The index of the recurrence to be mapped.
     * @return A pair of integers that can be used to identify which recurrence to delete.
     */
    fun map(
        recurrences: Recurrences,
        recurrenceIndex: Int,
    ): Pair<Int, Int> {
        return when (recurrences) {
            is Recurrences.Fixed -> recurrences.daysOfMonth[recurrenceIndex].value to 0
            is Recurrences.Variable -> recurrences.daysOfWeek[recurrenceIndex].value to 0
            is Recurrences.Seasonal -> {
                val dayAndMonth = recurrences.daysAndMonths[recurrenceIndex]
                dayAndMonth.day.value to dayAndMonth.month.value
            }
        }
    }
}
