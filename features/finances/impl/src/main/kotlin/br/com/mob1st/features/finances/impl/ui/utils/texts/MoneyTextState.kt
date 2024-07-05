package br.com.mob1st.features.finances.impl.ui.utils.texts

import android.icu.text.NumberFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.ui.utils.parcelers.MoneyParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import java.util.Locale

/**
 * Displays a [Money] object as text.
 */
@Immutable
@Parcelize
data class MoneyTextState(
    val money: @WriteWith<MoneyParceler> Money,
) : TextState {
    @Composable
    override fun resolve(): String {
        val currentLocale = LocalContext.current.resources.configuration.locales[0]
        return remember(money, currentLocale) {
            money.toCurrencyString(currentLocale)
        }
    }
}

/**
 * Converts a [Money] object to a currency string.
 * @param locale The locale to format the currency.
 * @return The currency string.
 */
fun Money.toCurrencyString(
    locale: Locale,
): String {
    val numberFormat = NumberFormat.getCurrencyInstance(locale)
    return numberFormat.format(cents / Money.SCALE)
}
