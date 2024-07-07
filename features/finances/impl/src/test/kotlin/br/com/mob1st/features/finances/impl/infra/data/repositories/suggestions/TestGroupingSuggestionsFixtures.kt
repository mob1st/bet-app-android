package br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.SelectSuggestions
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayAndMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.Month
import com.appmattus.kotlinfixture.Fixture

class TestGroupingSuggestionsFixtures(
    fixture: Fixture,
) {
    val suggestionsWithLinkedCategory = listOf(
        fixture<SelectSuggestions>().copy(
            sug_id = 1,
            sug_name = "rent_or_mortgage",
            cat_id = 1,
            cat_name = "Rent",
            cat_is_expense = true,
            cat_amount = 300_000,
            cat_linked_suggestion_id = 1,
            frc_day_of_month = null,
            src_month = 1,
            src_day = 1,
        ),
        fixture<SelectSuggestions>().copy(
            sug_id = 1,
            sug_name = "rent_or_mortgage",
            cat_id = 1,
            cat_name = "Rent",
            cat_is_expense = true,
            cat_amount = 300_000,
            cat_linked_suggestion_id = 1,
            frc_day_of_month = null,
            src_month = 1,
            src_day = 2,
        ),
        fixture<SelectSuggestions>().copy(
            sug_id = 2,
            sug_name = "gym",
            cat_id = 2,
            cat_name = "Gym",
            cat_is_expense = true,
            cat_linked_suggestion_id = 2,
            cat_amount = 2500,
            frc_day_of_month = null,
            src_month = 1,
            src_day = 2,
        ),
    )
    val suggestionsWithoutLinkedCategory = listOf(
        fixture<SelectSuggestions>().copy(
            sug_id = 3,
            sug_name = "public_transport",
            cat_id = null,
        ),
        fixture<SelectSuggestions>().copy(
            sug_id = 4,
            sug_name = "health_insurance",
            cat_id = null,
        ),
    )

    fun expected(
        nameSuggestion1: Int,
        nameSuggestion2: Int,
        nameSuggestion3: Int,
        nameSuggestion4: Int,
    ) = listOf(
        CategorySuggestion(
            id = CategorySuggestion.Id(1),
            nameResId = nameSuggestion1,
            linkedCategory = Category(
                id = Category.Id(1),
                name = "Rent",
                isExpense = true,
                amount = Money(3000_00),
                recurrences = Recurrences.Seasonal(
                    listOf(
                        DayAndMonth(
                            DayOfMonth(1),
                            Month(1),
                        ),
                        DayAndMonth(
                            DayOfMonth(2),
                            Month(1),
                        ),
                    ),
                ),
            ),
        ),
        CategorySuggestion(
            id = CategorySuggestion.Id(2),
            nameResId = nameSuggestion2,
            linkedCategory = Category(
                id = Category.Id(2),
                name = "Gym",
                isExpense = true,
                amount = Money(25_00),
                recurrences = Recurrences.Seasonal(
                    listOf(
                        DayAndMonth(
                            DayOfMonth(2),
                            Month(1),
                        ),
                    ),
                ),
            ),
        ),
        CategorySuggestion(
            id = CategorySuggestion.Id(3),
            nameResId = nameSuggestion3,
            linkedCategory = null,
        ),
        CategorySuggestion(
            id = CategorySuggestion.Id(4),
            nameResId = nameSuggestion4,
            linkedCategory = null,
        ),
    )
}
