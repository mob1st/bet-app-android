package br.com.mob1st.features.finances.impl.domain.entities

sealed interface GetCategoryIntent {
    val name: String

    data class Edit(
        val id: Category.Id,
        override val name: String,
    ) : GetCategoryIntent {
        constructor(category: Category) : this(
            id = category.id,
            name = category.name,
        )
    }

    data class Create(
        override val name: String,
        val defaultValues: CategoryDefaultValues,
    ) : GetCategoryIntent
}

data class CategoryDefaultValues(
    val isExpense: Boolean,
    val recurrenceType: RecurrenceType,
)
