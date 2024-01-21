package br.com.mob1st.features.finances.impl.domain.providers

import br.com.mob1st.features.finances.impl.data.ram.RecurrentCategorySuggestion

/**
 * Provides localized strings for recurrence suggestions
 */
internal interface RecurrenceLocalizationProvider {

    /**
     * Get the localized description for the given [suggestion]
     * @param suggestion the suggestion to be got
     */
    operator fun get(suggestion: RecurrentCategorySuggestion): String
}
