package br.com.mob1st.features.finances.impl.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import br.com.mob1st.core.design.motion.topLevel
import br.com.mob1st.features.finances.publicapi.domain.ui.CashFlowNavGraph
import kotlinx.serialization.Serializable

object CashFlowNavGraphImpl : CashFlowNavGraph {
    override val root: CashFlowNavGraph.Root = Root

    context(NavGraphBuilder)
    override fun graph(onClickClose: () -> Unit) {
        navigation<Root>(
            startDestination = CashFlowTarget.MainTab,
        ) {
            topLevel<CashFlowTarget.MainTab> {
                CashFlowTabPage(
                    onClickClose = onClickClose,
                )
            }
        }
    }

    @Serializable
    data object Root : CashFlowNavGraph.Root
}
