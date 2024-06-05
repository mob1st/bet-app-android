package br.com.mob1st.features.finances.publicapi.domain.ui

import androidx.navigation.NavGraphBuilder

interface FinancesNavGraph {
    val root: Root

    context(NavGraphBuilder)
    fun graph(onClickClose: () -> Unit)

    interface Root
}
