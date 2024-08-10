package br.com.mob1st.features.finances.impl.ui.category.detail

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.LocalizedText
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.ui.utils.texts.MoneyLocalizedText

@Immutable
sealed interface CategoryDetailState {
    val isDone: Boolean

    data object Loading : CategoryDetailState {
        override val isDone: Boolean = false
    }

    data class Loaded(
        val category: Category,
        val entry: CategoryEntry = CategoryEntry(),
        override val isDone: Boolean = false,
    ) : CategoryDetailState {
        val amount: LocalizedText = MoneyLocalizedText(
            entry.amount ?: category.amount,
        )
    }
}
