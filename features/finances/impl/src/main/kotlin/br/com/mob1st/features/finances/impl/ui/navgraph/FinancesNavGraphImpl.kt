package br.com.mob1st.features.finances.impl.ui.navgraph

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.mob1st.features.finances.publicapi.domain.ui.FinancesNavGraph
import kotlinx.serialization.Serializable

internal object FinancesNavGraphImpl : FinancesNavGraph {
    context(NavGraphBuilder)
    override fun graph(
        navController: NavController,
        onClickClose: () -> Unit,
    ) {
        composable("finances") {
            Text(text = "hey papito")
        }
    }

    @Serializable
    data object Main
}
