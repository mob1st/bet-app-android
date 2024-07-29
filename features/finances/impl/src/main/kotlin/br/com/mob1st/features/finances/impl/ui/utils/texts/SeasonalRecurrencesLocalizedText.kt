package br.com.mob1st.features.finances.impl.ui.utils.texts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.util.fastMap
import br.com.mob1st.core.design.atoms.properties.texts.LocalizedText
import br.com.mob1st.features.finances.impl.R
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfYear
import br.com.mob1st.features.finances.impl.domain.values.formatOfPattern
import br.com.mob1st.features.finances.impl.ui.utils.parcelers.RecurrencesParceler
import kotlinx.collections.immutable.toImmutableList
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import java.util.Locale

@Parcelize
@Immutable
data class SeasonalRecurrencesLocalizedText(
    val recurrences: @WriteWith<RecurrencesParceler> Recurrences.Seasonal,
) : LocalizedText {
    @IgnoredOnParcel
    private val immutableDaysAndMonths = recurrences.daysOfYear.toImmutableList()

    @Composable
    override fun resolve(): String {
        val locale = LocalContext.current.resources.configuration.locales[0]
        val textState = remember(immutableDaysAndMonths, locale) {
            immutableDaysAndMonths.resolve(locale)
        }
        return textState.resolve()
    }
}

private fun List<DayOfYear>.resolve(locale: Locale): LocalizedText {
    if (isEmpty()) {
        return LocalizedText("")
    }
    val arguments = when (size) {
        1, 2 -> fastMap {
            LocalizedText(
                it.formatToMonth(locale),
            )
        }

        else -> listOf(
            LocalizedText(toTextStateFirstItems(locale)),
            LocalizedText(
                last().formatToMonth(locale),
            ),
        )
    }
    return LocalizedText(
        R.plurals.finances_suggestion_seasonal_supporting,
        arguments.size,
        arguments,
    )
}

private fun List<DayOfYear>.toTextStateFirstItems(
    locale: Locale,
): String {
    return dropLast(1).joinToString(", ") {
        it.formatToMonth(locale)
    }
}

private fun DayOfYear.formatToMonth(locale: Locale) =
    formatOfPattern("MMM", locale).uppercase(locale)
