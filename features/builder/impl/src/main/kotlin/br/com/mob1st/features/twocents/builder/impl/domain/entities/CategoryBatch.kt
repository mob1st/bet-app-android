package br.com.mob1st.features.twocents.builder.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType

internal data class CategoryBatch(
    val categoryType: CategoryType,
    val inputs: List<CategoryInput>,
)

internal data class CategoryInput(
    val type: CategoryType,
    val name: String = "",
    val value: Money = Money.Zero,
    val linkedSuggestion: CategorySuggestion? = null,
) {
    fun calculateTotal(): Money {
        val multiplier: Float = when (type) {
            CategoryType.Fixed -> MONTH
            CategoryType.Variable -> WEEKS_PER_MONTH
            CategoryType.Seasonal -> YEAR_PER_MONTH
        }
        return value * multiplier
    }

    companion object {
        private const val MONTH = 1f
        private const val WEEKS_PER_MONTH = 4f
        private const val YEAR_PER_MONTH = 0.12f
    }
}
