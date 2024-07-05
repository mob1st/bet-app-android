package br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.features.finances.impl.SelectSuggestions
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import com.appmattus.kotlinfixture.Fixture

class TestTwoCategoriesWithSameSuggestionFixtures(
    fixture: Fixture,
) {
    val firstRegistryWithCategory = fixture<SelectSuggestions>().copy(
        sug_id = 1,
        sug_name = "first",
        cat_id = 1,
        cat_name = "cat1",
        cat_amount = 1000_00,
        cat_is_expense = true,
        cat_linked_suggestion_id = 1,
    )
    val secondRegistryWithCategory = fixture<SelectSuggestions>().copy(
        sug_id = 1,
        sug_name = "first",
        cat_id = 2,
        cat_name = "cat2",
        cat_amount = 3000_0,
        cat_is_expense = true,
        cat_linked_suggestion_id = 1,
    )
    val thirdRegistryNoCategory = fixture<SelectSuggestions>().copy(
        sug_id = 2,
        sug_name = "third",
        cat_id = null,
    )

    fun expected(
        recurrencesSuggestion1: Recurrences,
        nameResIdSuggestion1: Int,
        nameResIdSuggestion3: Int,
    ) = listOf(
        CategorySuggestion(
            id = RowId(1),
            nameResId = nameResIdSuggestion1,
            linkedCategory = Category(
                id = RowId(1),
                name = "cat1",
                isExpense = true,
                amount = Money(1000_00),
                recurrences = recurrencesSuggestion1,
            ),
        ),
        CategorySuggestion(
            id = RowId(2),
            nameResId = nameResIdSuggestion3,
            linkedCategory = null,
        ),
    )
}
