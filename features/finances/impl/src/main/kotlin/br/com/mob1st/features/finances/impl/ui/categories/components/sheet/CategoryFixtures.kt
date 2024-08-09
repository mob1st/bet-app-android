package br.com.mob1st.features.finances.impl.ui.categories.components.sheet

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences

@Suppress("MagicNumber")
object CategoryFixtures {
    val category = Category(
        name = "name",
        amount = Money(100),
        recurrences = Recurrences.Variable,
        image = Uri("image"),
        isSuggested = true,
        isExpense = true,
    )
}
