package br.com.mob1st.features.finances.impl.ui

import kotlinx.serialization.Serializable

sealed interface FinancesNavTarget {
    @Serializable
    data class OperationsList(
        val type: Int,
    ) : FinancesNavTarget
}
