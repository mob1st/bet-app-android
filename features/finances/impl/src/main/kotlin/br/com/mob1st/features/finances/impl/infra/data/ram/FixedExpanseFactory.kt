package br.com.mob1st.features.finances.impl.infra.data.ram

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.infra.data.ram.FixedExpanseFactory.suggestions
import br.com.mob1st.features.finances.impl.infra.data.system.RecurrenceLocalizationProvider
import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItem
import br.com.mob1st.features.finances.publicapi.domain.entities.Recurrence
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory

/**
 * Factory for [RecurrentCategory] from hardcoded [suggestions].
 */
internal object FixedExpanseFactory : RecurrenceSuggestionFactory() {
    override val suggestions: List<RecurrentCategorySuggestion> =
        listOf(
            RecurrentCategorySuggestion.RENT,
            RecurrentCategorySuggestion.MORTGAGE,
            RecurrentCategorySuggestion.ENERGY,
            RecurrentCategorySuggestion.INTERNET,
            RecurrentCategorySuggestion.PHONE,
            RecurrentCategorySuggestion.TV_STREAMING,
            RecurrentCategorySuggestion.MUSIC_STREAMING,
            RecurrentCategorySuggestion.INSURANCES,
            RecurrentCategorySuggestion.EDUCATION,
            RecurrentCategorySuggestion.GYM,
            RecurrentCategorySuggestion.TRANSPORT,
        )

    override fun invoke(localizationProvider: RecurrenceLocalizationProvider): List<RecurrentCategory> {
        return suggestions.map { suggestion ->
            RecurrentCategory(
                description = localizationProvider[suggestion],
                amount = Money.Zero,
                type = BudgetItem.Type.EXPENSE,
                recurrence = Recurrence.Fixed(),
            )
        }
    }
}
