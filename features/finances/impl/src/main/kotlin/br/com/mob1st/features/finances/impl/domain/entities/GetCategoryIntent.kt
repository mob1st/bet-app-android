package br.com.mob1st.features.finances.impl.domain.entities

import kotlinx.serialization.Serializable

@Serializable
sealed interface GetCategoryIntent {
    val name: String

    @Serializable
    data class Edit(
        val id: Category.Id,
        override val name: String,
    ) : GetCategoryIntent

    @Serializable
    data class Create(override val name: String) : GetCategoryIntent
}
