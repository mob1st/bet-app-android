package br.com.mob1st.features.finances.impl.data.preferences

/**
 * Indicates which steps of the recurrence builder are completed.
 */
internal data class RecurrenceBuilderCompletions(
    val isFixedExpansesCompleted: Boolean = false,
    val isVariableExpansesCompleted: Boolean = false,
    val isSeasonalExpansesCompleted: Boolean = false,
    val isIncomesCompleted: Boolean = false,
)
