package br.com.mob1st.features.finances.impl.ui.utils.texts

import android.icu.text.MessageFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.features.finances.impl.R
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.ui.utils.parcelers.FixedRecurrencesParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import java.util.Locale

@Parcelize
@Immutable
data class FixedRecurrencesTextState(
    val recurrences: @WriteWith<FixedRecurrencesParceler> Recurrences.Fixed,
) : TextState {
    @Composable
    override fun resolve(): String {
        val locale = LocalContext.current.resources.configuration.locales[0]
        val textState = remember(recurrences.day, locale) {
            recurrences.toTextState(locale)
        }
        return textState.resolve()
    }
}

private fun Recurrences.Fixed.toTextState(locale: Locale): TextState {
    val messageFormat = MessageFormat.format("{0,ordinal}", locale)
    val ordinalNumber = TextState(messageFormat.format(day))
    return TextState(
        R.string.finances_category_fixed_recurrence_item_supporting,
        listOf(ordinalNumber),
    )
}
