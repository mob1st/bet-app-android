package br.com.mob1st.features.finances.impl.ui.navgraph

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.mob1st.features.finances.publicapi.domain.ui.FinancesNavGraph
import kotlinx.serialization.Serializable

internal object FinancesNavGraphImpl : FinancesNavGraph {
    override val root: FinancesNavGraph.Root = Root

    context(NavGraphBuilder)
    override fun graph(
        navController: NavController,
        onClickClose: () -> Unit,
    ) {
        navigation<Root>(
            startDestination = "main",
        ) {
            composable("main") {
                Text(text = "Main")
            }
        }
    }

    @Serializable
    data object Root : FinancesNavGraph.Root
}
