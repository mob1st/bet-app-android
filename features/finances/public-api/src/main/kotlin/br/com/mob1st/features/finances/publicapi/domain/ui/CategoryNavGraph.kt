package br.com.mob1st.features.finances.publicapi.domain.ui

import androidx.navigation.NavGraphBuilder

interface CategoryNavGraph {
    context(NavGraphBuilder)
    fun graph()
}
