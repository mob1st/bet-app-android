package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Identifiable
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.features.finances.impl.domain.values.DayAndMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth

data class Category(
    override val id: RowId = RowId(),
    val name: String,
    val amount: Money,
    val isExpense: Boolean,
    val recurrences: Recurrences,
) : Identifiable<RowId>

sealed interface Recurrences {
    data class Fixed(
        val day: DayOfMonth,
    ) : Recurrences

    data object Variable : Recurrences

    data class Seasonal(
        val daysAndMonths: List<DayAndMonth>,
    ) : Recurrences
}
