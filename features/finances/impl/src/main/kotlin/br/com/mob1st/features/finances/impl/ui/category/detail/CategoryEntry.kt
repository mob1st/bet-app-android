package br.com.mob1st.features.finances.impl.ui.category.detail

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences

data class CategoryEntry(
    val name: String? = null,
    val amount: Money? = null,
    val recurrences: Recurrences? = null,
    val image: Uri? = null,
)
