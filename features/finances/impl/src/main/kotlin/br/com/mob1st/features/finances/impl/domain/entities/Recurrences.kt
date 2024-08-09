package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfYear

/**
 * The number of times an expense or income happens.
 */
sealed interface Recurrences {
    /**
     * Converts this instance to a [RecurrenceType].
     */
    fun asType(): RecurrenceType

    /**
     * Fixed recurrences happen on a specific day of the month and usually doesn't change its amount based on how
     * much consumed or used it was.
     * @property day The day of the month when the expense or income happens.
     */
    data class Fixed(
        val day: DayOfMonth,
    ) : Recurrences {
        override fun asType(): RecurrenceType = RecurrenceType.Fixed
    }

    /**
     * Variable recurrences doesn't have a fixed day of the month.
     * They typically have their amount based on how much consumed or used it was.
     */
    data object Variable : Recurrences {
        override fun asType(): RecurrenceType = RecurrenceType.Variable
    }

    /**
     * Seasonal recurrences have a lower frequency in the year but it's still predictable (and good to plan for).
     * @property daysOfYear The days of the year when the expense or income happens. It can be empty, which means that
     * the expense or income doesn't happen in a specific moment in the year (but it still happens).
     */
    data class Seasonal(
        val daysOfYear: List<DayOfYear>,
    ) : Recurrences {
        override fun asType(): RecurrenceType = RecurrenceType.Seasonal
    }
}

/**
 * The type of recurrences.
 */
enum class RecurrenceType {
    Fixed,
    Variable,
    Seasonal,
}

/**
 * Converts this instance to a [Recurrences] using default values for initialization.
 * @return The converted instance.
 */
internal fun RecurrenceType.toDefaultRecurrences(): Recurrences {
    return when (this) {
        RecurrenceType.Fixed -> Recurrences.Fixed(DayOfMonth(1))
        RecurrenceType.Variable -> Recurrences.Variable
        RecurrenceType.Seasonal -> Recurrences.Seasonal(emptyList())
    }
}
