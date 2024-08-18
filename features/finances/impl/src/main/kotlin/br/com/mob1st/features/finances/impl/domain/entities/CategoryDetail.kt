package br.com.mob1st.features.finances.impl.domain.entities

/**
 * Holds the data required to show the category detail screen.
 * @property category The category to be shown.
 * @property preferences The preferences of the calculator.
 */
data class CategoryDetail(
    val category: Category,
    val preferences: CalculatorPreferences,
)
