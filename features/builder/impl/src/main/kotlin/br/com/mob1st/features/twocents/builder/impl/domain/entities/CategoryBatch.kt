package br.com.mob1st.features.twocents.builder.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money

internal data class CategoryBatch(
    val inputs: List<CategoryInput>,
)

internal data class CategoryInput(
    val name: String,
    val value: Money,
    val linkedSuggestion: CategorySuggestion?,
)
