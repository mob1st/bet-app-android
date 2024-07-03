package br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions

import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.features.finances.impl.SelectSuggestions
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import com.appmattus.kotlinfixture.Fixture

class TestDiscardNullFixtures(
    fixture: Fixture,
) {
    val suggestionWithCategory = fixture<SelectSuggestions>().copy(
        sug_id = 1,
        cat_name = null,
        cat_id = 1,
        cat_is_expense = true,
        cat_amount = 300_000,
        cat_linked_suggestion_id = 1,
    )
    val suggestionWithoutCategory = fixture<SelectSuggestions>().copy(
        sug_id = 2,
        cat_id = null,
    )

    fun expected(nameResId: Int) = listOf(
        CategorySuggestion(
            id = RowId(2),
            nameResId = nameResId,
            linkedCategory = null,
        ),
    )
}
