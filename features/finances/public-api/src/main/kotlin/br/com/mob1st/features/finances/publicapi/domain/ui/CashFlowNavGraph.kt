package br.com.mob1st.features.finances.publicapi.domain.ui

import androidx.navigation.NavGraphBuilder

interface CashFlowNavGraph {
    val root: Root

    context(NavGraphBuilder)
    fun graph(onClickClose: () -> Unit)

    interface Root
}
