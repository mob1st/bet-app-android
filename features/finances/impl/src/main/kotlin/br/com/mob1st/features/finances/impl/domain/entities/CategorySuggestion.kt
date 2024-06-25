package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Identifiable
import br.com.mob1st.core.kotlinx.structures.RowId

data class CategorySuggestion(
    override val id: RowId,
    val name: Name,
    val linkedCategory: Category? = null,
) : Identifiable<RowId> {
    enum class Name {
        Rent,
    }
}
