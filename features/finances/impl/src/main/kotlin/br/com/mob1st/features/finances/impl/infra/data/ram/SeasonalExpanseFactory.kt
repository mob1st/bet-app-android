package br.com.mob1st.features.finances.impl.infra.data.ram

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.infra.data.ram.SeasonalExpanseFactory.suggestions
import br.com.mob1st.features.finances.impl.infra.data.system.RecurrenceLocalizationProvider
import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItem
import br.com.mob1st.features.finances.publicapi.domain.entities.Recurrence
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import kotlinx.datetime.Month

/**
 * Factory for [RecurrentCategory] from hardcoded [suggestions].
 */
internal object SeasonalExpanseFactory : RecurrenceSuggestionFactory() {
    override val suggestions: List<RecurrentCategorySuggestion> =
        listOf(
            RecurrentCategorySuggestion.HOLIDAYS,
            RecurrentCategorySuggestion.CHRISTMAS,
            RecurrentCategorySuggestion.GIFTS,
            RecurrentCategorySuggestion.CLOTHES,
            RecurrentCategorySuggestion.ELECTRONICS,
            RecurrentCategorySuggestion.FURNITURE,
        )

    override fun invoke(localizationProvider: RecurrenceLocalizationProvider): List<RecurrentCategory> {
        return suggestions.map { suggestion ->
            RecurrentCategory(
                description = localizationProvider[suggestion],
                amount = Money.Zero,
                type = BudgetItem.Type.EXPENSE,
                recurrence =
                Recurrence.Seasonal(
                    month = Month.JANUARY,
                    day = 1,
                ),
            )
        }
    }
}
