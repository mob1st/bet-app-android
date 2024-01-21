package br.com.mob1st.features.finances.impl.data.ram

import br.com.mob1st.features.finances.impl.domain.providers.RecurrenceLocalizationProvider
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory

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
