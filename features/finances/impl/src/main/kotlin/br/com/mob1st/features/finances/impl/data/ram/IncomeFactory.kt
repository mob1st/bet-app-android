package br.com.mob1st.features.finances.impl.data.ram

import br.com.mob1st.features.finances.impl.data.system.RecurrenceLocalizationProvider
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory

/**
 * Create a list of [RecurrentCategory] from a hardcoded list of [RecurrentCategorySuggestion]
 */
internal object IncomeFactory : RecurrenceSuggestionFactory() {
    override val suggestions: List<RecurrentCategorySuggestion> = emptyList()

    override fun invoke(localizationProvider: RecurrenceLocalizationProvider): List<RecurrentCategory> {
        return emptyList()
    }
}
