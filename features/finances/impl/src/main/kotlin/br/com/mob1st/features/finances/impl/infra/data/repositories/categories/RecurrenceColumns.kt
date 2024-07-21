package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfYear
import java.util.Locale

private const val COLUMN_SEPARATOR = ","
private val defaultLocale = Locale.US

/**
 * Holds the columns type and recurrences from Categories table.
 * It can be used to convert this two columns to and from [Recurrences] domain entity.
 * @property rawType the type of the recurrence, in a raw format saved in the database.
 * @property rawRecurrences the recurrences in a raw format saved in the database.
 */
internal data class RecurrenceColumns(
    val rawType: String,
    val rawRecurrences: String?,
) {
    companion object {
        /**
         * Creates a [RecurrenceColumns] from the given [recurrences].
         * It's useful to write the [Recurrences] to the database.
         * @param recurrences the [Recurrences] domain entity.
         * @return the [RecurrenceColumns] created from the given [recurrences].
         */
        fun from(recurrences: Recurrences): RecurrenceColumns {
            return recurrences.toRecurrenceColumns()
        }
    }
}

/**
 * Converts the raw columns to a [Recurrences] domain entity.
 * It's useful to read the [Recurrences] from the database.
 * @return the [Recurrences] domain entity.
 */
internal fun RecurrenceColumns.toRecurrences(): Recurrences {
    return when (RecurrenceType.fromValue(rawType)) {
        RecurrenceType.Fixed -> toFixedRecurrence()
        RecurrenceType.Seasonal -> toSeasonalRecurrence()
        RecurrenceType.Variable -> Recurrences.Variable
    }
}

private fun RecurrenceColumns.toFixedRecurrence(): Recurrences.Fixed {
    val dayOfMonth = DayOfMonth(checkNotNull(rawRecurrences).toInt())
    return Recurrences.Fixed(dayOfMonth)
}

private fun RecurrenceColumns.toSeasonalRecurrence(): Recurrences.Seasonal {
    if (rawRecurrences == null) {
        return Recurrences.Seasonal(emptyList())
    }
    val daysOfYear = rawRecurrences.split(COLUMN_SEPARATOR).map {
        DayOfYear(it.toInt())
    }
    return Recurrences.Seasonal(daysOfYear)
}

private fun Recurrences.toRecurrenceColumns(): RecurrenceColumns {
    return when (this) {
        is Recurrences.Fixed -> RecurrenceColumns(
            RecurrenceType.Fixed.value,
            toRawRecurrences(),
        )

        is Recurrences.Seasonal -> RecurrenceColumns(
            RecurrenceType.Seasonal.value,
            toRawRecurrences(),
        )

        Recurrences.Variable -> RecurrenceColumns(
            RecurrenceType.Variable.value,
            null,
        )
    }
}

private fun Recurrences.Seasonal.toRawRecurrences(): String {
    return daysOfYear.joinToString(COLUMN_SEPARATOR) {
        String.format(defaultLocale, "%03d", it.value)
    }
}

private fun Recurrences.Fixed.toRawRecurrences(): String {
    return String.format(defaultLocale, "%02d", day.value)
}
