package br.com.mob1st.features.finances.impl.data.ram

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.domain.providers.RecurrenceLocalizationProvider
import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItem
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentTransaction
import kotlinx.datetime.Month

internal object SeasonalExpanseFactory : RecurrenceSuggestionFactory<RecurrentCategory.Seasonal>() {

    override val suggestions: List<RecurrentCategorySuggestion> = listOf(
        RecurrentCategorySuggestion.HOLIDAYS,
        RecurrentCategorySuggestion.CHRISTMAS,
        RecurrentCategorySuggestion.GIFTS,
        RecurrentCategorySuggestion.CLOTHES,
        RecurrentCategorySuggestion.ELECTRONICS,
        RecurrentCategorySuggestion.FURNITURE
    )

    override fun invoke(localizationProvider: RecurrenceLocalizationProvider): List<RecurrentCategory.Seasonal> {
        return suggestions.map { suggestion ->
            RecurrentCategory.Seasonal(
                recurrentTransaction = RecurrentTransaction(
                    description = localizationProvider[suggestion],
                    amount = Money.Zero,
                    type = BudgetItem.Type.EXPENSE
                ),
                month = Month.JANUARY,
                day = RecurrentCategory.DEFAULT_DAY_OF_MONTH
            )
        }
    }
}
