package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Identifiable
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.fixtures.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.fixtures.DayOfYear

data class Category(
    override val id: Id = Id(),
    val name: String,
    val image: Uri,
    val amount: Money,
    val isExpense: Boolean,
    val recurrences: Recurrences,
    val isSuggested: Boolean,
) : Identifiable<Category.Id> {
    @JvmInline
    value class Id(override val value: Long = 0) : RowId
}

sealed interface Recurrences {
    data class Fixed(
        val day: DayOfMonth,
    ) : Recurrences

    data object Variable : Recurrences

    data class Seasonal(
        val daysOfYear: List<DayOfYear>,
    ) : Recurrences
}
