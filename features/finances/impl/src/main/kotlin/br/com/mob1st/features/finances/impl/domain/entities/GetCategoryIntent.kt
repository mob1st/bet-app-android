package br.com.mob1st.features.finances.impl.domain.entities

sealed interface GetCategoryIntent {
    val type: RecurrenceType
    val isExpense: Boolean

    data class Edit(
        val id: Category.Id,
        val isSuggested: Boolean,
        override val type: RecurrenceType,
        override val isExpense: Boolean,
    ) : GetCategoryIntent

    data class Create(
        val name: String,
        override val type: RecurrenceType,
        override val isExpense: Boolean,
    ) : GetCategoryIntent
}
