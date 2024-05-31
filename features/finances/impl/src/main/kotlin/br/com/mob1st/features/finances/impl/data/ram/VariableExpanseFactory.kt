package br.com.mob1st.features.finances.impl.data.ram

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.data.ram.VariableExpanseFactory.suggestions
import br.com.mob1st.features.finances.impl.data.system.RecurrenceLocalizationProvider
import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItem
import br.com.mob1st.features.finances.publicapi.domain.entities.Recurrence
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory

/**
 * Factory for [RecurrentCategory] from hardcoded [suggestions].
 */
internal object VariableExpanseFactory : RecurrenceSuggestionFactory() {
    override val suggestions: List<RecurrentCategorySuggestion> =
        listOf(
            RecurrentCategorySuggestion.LUNCH,
            RecurrentCategorySuggestion.WEEKEND_RESTAURANT,
            RecurrentCategorySuggestion.FOOD_DELIVERY,
            RecurrentCategorySuggestion.BARS,
            RecurrentCategorySuggestion.ENTERTAINMENT,
            RecurrentCategorySuggestion.BEAUTY,
        )

    override fun invoke(localizationProvider: RecurrenceLocalizationProvider): List<RecurrentCategory> {
        return suggestions
            .map { suggestion ->
                RecurrentCategory(
                    description = localizationProvider[suggestion],
                    amount = Money.Zero,
                    type = BudgetItem.Type.EXPENSE,
                    recurrence = Recurrence.Variable,
                )
            }
    }
}
