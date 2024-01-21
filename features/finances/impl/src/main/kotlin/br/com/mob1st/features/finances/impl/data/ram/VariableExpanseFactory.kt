package br.com.mob1st.features.finances.impl.data.ram

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.domain.providers.RecurrenceLocalizationProvider
import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItem
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentTransaction

internal object VariableExpanseFactory : RecurrenceSuggestionFactory<RecurrentCategory.Variable>() {
    override val suggestions: List<RecurrentCategorySuggestion> = listOf(
        RecurrentCategorySuggestion.LUNCH,
        RecurrentCategorySuggestion.WEEKEND_RESTAURANT,
        RecurrentCategorySuggestion.FOOD_DELIVERY,
        RecurrentCategorySuggestion.BARS,
        RecurrentCategorySuggestion.ENTERTAINMENT,
        RecurrentCategorySuggestion.BEAUTY
    )

    override fun invoke(localizationProvider: RecurrenceLocalizationProvider): List<RecurrentCategory.Variable> {
        return suggestions
            .map { suggestion ->
                RecurrentCategory.Variable(
                    recurrentTransaction = RecurrentTransaction(
                        description = localizationProvider[suggestion],
                        amount = Money.Zero,
                        type = BudgetItem.Type.EXPENSE
                    )
                )
            }
    }
}
