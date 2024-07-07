package br.com.mob1st.features.finances.impl.ui.utils.texts

import android.icu.text.DateFormatSymbols
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.util.fastMap
import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.features.finances.impl.R
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayAndMonth
import br.com.mob1st.features.finances.impl.ui.utils.parcelers.RecurrencesParceler
import kotlinx.collections.immutable.toImmutableList
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import java.util.Locale

@Parcelize
@Immutable
data class SeasonalRecurrencesTextState(
    val recurrences: @WriteWith<RecurrencesParceler> Recurrences.Seasonal,
) : TextState {
    @IgnoredOnParcel
    private val immutableDaysAndMonths = recurrences.daysAndMonths.toImmutableList()

    @Composable
    override fun resolve(): String {
        val locale = LocalContext.current.resources.configuration.locales[0]
        val textState = remember(immutableDaysAndMonths, locale) {
            immutableDaysAndMonths.resolve(locale)
        }
        return textState.resolve()
    }
}

private fun List<DayAndMonth>.resolve(locale: Locale): TextState {
    val symbols = DateFormatSymbols.getInstance(locale)
    val arguments = when (size) {
        1, 2 -> fastMap {
            TextState(it.toShortMonth(locale, symbols))
        }

        else -> listOf(
            TextState(toTextStateFirstItems(locale, symbols)),
            TextState(last().toShortMonth(locale, symbols)),
        )
    }
    return TextState(
        R.plurals.finances_builder_not_enough_items_error,
        arguments.size,
        arguments,
    )
}

private fun List<DayAndMonth>.toTextStateFirstItems(
    locale: Locale,
    symbols: DateFormatSymbols,
): String {
    return dropLast(1).joinToString(", ") {
        it.toShortMonth(locale, symbols)
    }
}

private fun DayAndMonth.toShortMonth(
    locale: Locale,
    symbols: DateFormatSymbols,
): String = symbols.shortMonths[month.value - 1].uppercase(locale)
