package br.com.mob1st.features.finances.impl.domain.entities

sealed interface GetCategoryIntent {
    data class Edit(val id: Category.Id) : GetCategoryIntent

    data class Create(val name: String) : GetCategoryIntent
}
