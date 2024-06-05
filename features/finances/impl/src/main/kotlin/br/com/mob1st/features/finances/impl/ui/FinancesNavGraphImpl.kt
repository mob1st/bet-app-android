package br.com.mob1st.features.finances.impl.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import br.com.mob1st.core.design.motion.topLevel
import br.com.mob1st.features.finances.publicapi.domain.ui.FinancesNavGraph
import kotlinx.serialization.Serializable

object FinancesNavGraphImpl : FinancesNavGraph {
    override val root: FinancesNavGraph.Root = Root

    context(NavGraphBuilder)
    override fun graph(onClickClose: () -> Unit) {
        navigation<Root>(
            startDestination = FinancesNavTarget.OperationsList(1),
        ) {
            topLevel<FinancesNavTarget.OperationsList> {
                TransactionListTabPage(
                    onClickClose = onClickClose,
                )
            }
        }
    }

    @Serializable
    data object Root : FinancesNavGraph.Root
}
