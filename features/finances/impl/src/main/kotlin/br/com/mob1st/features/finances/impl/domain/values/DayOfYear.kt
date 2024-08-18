package br.com.mob1st.features.finances.impl.domain.values

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Any non leap year will work.
 * This is the year I was born :)
 */
private const val ARB_YEAR = 1991

@JvmInline
value class DayOfYear(val value: Int) {
    init {
        require(value in 1..DAYS_IN_A_YEAR) {
            "The day of year must be between 1 and $DAYS_IN_A_YEAR. Current value: $value."
        }
    }

    companion object {
        private const val DAYS_IN_A_YEAR = 365
    }
}

/**
 * Formats the day of year to a string using the given [pattern] and [locale].
 * It uses an arbitrary non-leap year to format the day of year, since the day of year is not dependent on the year.
 * @param pattern The pattern to format the day of year.
 * @param locale The locale to format the day of year.
 */
fun DayOfYear.formatOfPattern(
    pattern: String,
    locale: Locale,
): String {
    val date = LocalDate(ARB_YEAR, 1, 1)
    val newDate = date.plus(value - 1, DateTimeUnit.DAY)
    val formatter = DateTimeFormatter.ofPattern(pattern, locale)
    return newDate.toJavaLocalDate().format(formatter)
}

fun DayOfYear.Companion.fromMonth(month: Month): DayOfYear {
    val date = LocalDate(ARB_YEAR, month, 1)
    return DayOfYear(date.dayOfYear)
}

/**
 * Selects the month of the day of year.
 * @return The index of the month.
 */
fun DayOfYear.selectedMonth(): Int {
    return Month.entries.indexOfFirst { month ->
        val date = LocalDate(ARB_YEAR, month, month.length(false))
        date.dayOfYear >= value
    }
}
