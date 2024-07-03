package br.com.mob1st.features.finances.impl.domain.entities

import androidx.annotation.StringRes
import br.com.mob1st.core.kotlinx.structures.Identifiable
import br.com.mob1st.core.kotlinx.structures.RowId

data class CategorySuggestion(
    override val id: RowId,
    @StringRes val nameResId: Int,
    val linkedCategory: Category? = null,
) : Identifiable<RowId>
