package br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions

import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.features.finances.impl.SelectSuggestions
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import com.appmattus.kotlinfixture.Fixture

class TestUnknownNameFixtures(
    fixture: Fixture,
) {
    val suggestionWithCategory = fixture<SelectSuggestions>().copy(
        sug_id = 1,
        cat_id = 1,
        cat_is_expense = false,
        cat_amount = 1000_00,
        cat_linked_suggestion_id = 1,
    )
    val suggestionWithoutCategory = fixture<SelectSuggestions>().copy(
        sug_id = 2,
        cat_id = null,
    )

    fun expected(nameResId: Int) = listOf(
        CategorySuggestion(
            id = RowId(2),
            nameResId = 2,
            linkedCategory = null,
        ),
    )
}
