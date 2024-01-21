package br.com.mob1st.features.finances.impl.data.preferences

internal data class RecurrenceBuilderCompletions(
    val isFixedExpansesCompleted: Boolean = false,
    val isVariableExpansesCompleted: Boolean = false,
    val isSeasonalExpansesCompleted: Boolean = false,
    val isIncomesCompleted: Boolean = false,
)
