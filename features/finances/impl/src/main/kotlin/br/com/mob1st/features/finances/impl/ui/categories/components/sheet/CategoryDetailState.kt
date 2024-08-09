package br.com.mob1st.features.finances.impl.ui.categories.components.sheet

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.LocalizedText
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.ui.utils.texts.MoneyLocalizedText

@Immutable
data class CategoryDetailState(
    val category: Category? = null,
    val entry: CategoryEntry = CategoryEntry(),
    val isDone: Boolean = false,
) {
    val amount: LocalizedText = MoneyLocalizedText(
        category?.amount ?: Money.Zero,
    )
}
