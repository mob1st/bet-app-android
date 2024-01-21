package br.com.mob1st.features.finances.impl.data.ram

import br.com.mob1st.features.finances.impl.domain.providers.RecurrenceLocalizationProvider
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory

internal object IncomeFactory : RecurrenceSuggestionFactory<RecurrentCategory>() {
    override val suggestions: List<RecurrentCategorySuggestion> = emptyList()

    override fun invoke(localizationProvider: RecurrenceLocalizationProvider): List<RecurrentCategory> {
        return emptyList()
    }
}
