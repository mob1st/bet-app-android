package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfYear

/**
 * The number of times an expense or income happens.
 */
sealed interface Recurrences {
    /**
     * Fixed recurrences happen on a specific day of the month and usually doesn't change its amount based on how
     * much consumed or used it was.
     * @property day The day of the month when the expense or income happens.
     */
    data class Fixed(
        val day: DayOfMonth,
    ) : Recurrences

    /**
     * Variable recurrences doesn't have a fixed day of the month.
     * They typically have their amount based on how much consumed or used it was.
     */
    data object Variable : Recurrences

    /**
     * Seasonal recurrences have a lower frequency in the year but it's still predictable (and good to plan for).
     * @property daysOfYear The days of the year when the expense or income happens. It can be empty, which means that
     * the expense or income doesn't happen in a specific moment in the year (but it still happens).
     */
    data class Seasonal(
        val daysOfYear: List<DayOfYear>,
    ) : Recurrences
}

/**
 * The type of recurrences.
 */
enum class RecurrenceType {
    Fixed,
    Variable,
    Seasonal,
}
