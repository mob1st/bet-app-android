package br.com.mob1st.features.finances.impl.data.ram

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.data.ram.FixedExpanseFactory.suggestions
import br.com.mob1st.features.finances.impl.data.system.RecurrenceLocalizationProvider
import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItem
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentTransaction

/**
 * Factory for [RecurrentCategory.Fixed] from hardcoded [suggestions].
 */
internal object FixedExpanseFactory : RecurrenceSuggestionFactory<RecurrentCategory.Fixed>() {
    override val suggestions: List<RecurrentCategorySuggestion> = listOf(
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
        RecurrentCategorySuggestion.TRANSPORT
    )

    override fun invoke(localizationProvider: RecurrenceLocalizationProvider): List<RecurrentCategory.Fixed> {
        return suggestions.map { suggestion ->
            RecurrentCategory.Fixed(
                recurrentTransaction = RecurrentTransaction(
                    description = localizationProvider[suggestion],
                    amount = Money.Zero,
                    type = BudgetItem.Type.EXPENSE
                ),
                dayOfMonth = RecurrentCategory.DEFAULT_DAY_OF_MONTH
            )
        }
    }
}
