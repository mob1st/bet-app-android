package br.com.mob1st.features.finances.impl.ui.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import br.com.mob1st.core.design.motion.transition.target
import br.com.mob1st.features.finances.impl.ui.tabs.CashFlowTabPage
import br.com.mob1st.features.finances.impl.ui.tabs.CashFlowTarget
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
            startDestination = CashFlowTarget.MainTab,
        ) {
            target<CashFlowTarget.MainTab> {
                CashFlowTabPage(
                    onClickClose = onClickClose,
                )
            }
        }
    }

    @Serializable
    data object Root : FinancesNavGraph.Root
}
