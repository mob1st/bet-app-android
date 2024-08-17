package br.com.mob1st.features.finances.impl.domain.entities

data class CategoryDetail(
    val category: Category,
    val preferences: CalculatorPreferences,
) {
    val isCentsEnabled: Boolean = preferences.isCentsEnabled

    fun setIsCentsEnabled(isCentsEnabled: Boolean): CategoryDetail {
        return copy(preferences = preferences.copy(isCentsEnabled = isCentsEnabled))
    }
}
