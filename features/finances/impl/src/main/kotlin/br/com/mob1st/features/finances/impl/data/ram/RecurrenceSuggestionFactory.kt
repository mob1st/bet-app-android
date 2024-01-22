package br.com.mob1st.features.finances.impl.data.ram

import br.com.mob1st.features.finances.impl.data.system.RecurrenceLocalizationProvider
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory

/**
 * Create a list of [RecurrentCategory] from a hardcoded list of [RecurrentCategorySuggestion]
 * Ideally use objects to implement this it.
 */
internal abstract class RecurrenceSuggestionFactory<T : RecurrentCategory> {

    /**
     * The hardcoded list of suggestions
     */
    protected abstract val suggestions: List<RecurrentCategorySuggestion>

    /**
     * use the given [localizationProvider] to find the approprieate descriptions for the [suggestions]
     * @return a list of [RecurrentCategory]
     */
    abstract operator fun invoke(localizationProvider: RecurrenceLocalizationProvider): List<T>
}
