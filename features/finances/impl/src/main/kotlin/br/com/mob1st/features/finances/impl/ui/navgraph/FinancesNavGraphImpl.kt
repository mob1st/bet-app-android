package br.com.mob1st.features.finances.impl.ui.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import br.com.mob1st.features.finances.publicapi.domain.ui.FinancesNavGraph
import kotlinx.serialization.Serializable

internal object FinancesNavGraphImpl : FinancesNavGraph {
    context(NavGraphBuilder)
    override fun graph(
        navController: NavController,
        onClickClose: () -> Unit,
    ) {
    }

    @Serializable
    data object Main
}
