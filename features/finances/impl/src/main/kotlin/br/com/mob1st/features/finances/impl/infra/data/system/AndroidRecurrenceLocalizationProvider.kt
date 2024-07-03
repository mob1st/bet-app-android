package br.com.mob1st.features.finances.impl.infra.data.system

import android.content.Context
import br.com.mob1st.features.finances.impl.infra.data.ram.RecurrentCategorySuggestion

/**
 * Implementation of [RecurrenceLocalizationProvider] for Android using [Context.getString]
 * @property context the context to get the localized strings
 */
internal class AndroidRecurrenceLocalizationProvider(
    private val context: Context,
) : RecurrenceLocalizationProvider {
    override operator fun get(suggestion: RecurrentCategorySuggestion): String {
        TODO()
    }
}
