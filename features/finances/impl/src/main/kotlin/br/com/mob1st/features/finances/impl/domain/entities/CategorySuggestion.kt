package br.com.mob1st.features.finances.impl.domain.entities

import androidx.annotation.StringRes
import br.com.mob1st.core.kotlinx.structures.Identifiable
import br.com.mob1st.core.kotlinx.structures.RowId

data class CategorySuggestion(
    override val id: Id = Id(),
    @StringRes val nameResId: Int,
    val linkedCategory: Category? = null,
) : Identifiable<CategorySuggestion.Id> {
    @JvmInline
    value class Id(override val value: Long = 0) : RowId
}
