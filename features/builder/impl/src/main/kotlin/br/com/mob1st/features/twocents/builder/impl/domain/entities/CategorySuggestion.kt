package br.com.mob1st.features.twocents.builder.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Identifiable
import br.com.mob1st.core.kotlinx.structures.RowId

/**
 * The category suggestion.
 */
data class CategorySuggestion(
    override val id: RowId,
    val name: Name,
) : Identifiable<RowId> {
    enum class Name {
        RENT,
        PARTY,
    }
}
