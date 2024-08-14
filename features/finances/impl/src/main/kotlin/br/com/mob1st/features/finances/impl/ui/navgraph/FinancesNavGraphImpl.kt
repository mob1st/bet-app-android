package br.com.mob1st.features.finances.impl.ui.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import br.com.mob1st.features.finances.publicapi.domain.ui.BudgetBuilderNavGraph
import br.com.mob1st.features.finances.publicapi.domain.ui.CategoryNavGraph
import br.com.mob1st.features.finances.publicapi.domain.ui.FinancesNavGraph

internal class FinancesNavGraphImpl(
    private val categoryNavGraph: CategoryNavGraph,
    private val builderNavGraph: BudgetBuilderNavGraph,
) : FinancesNavGraph {
    context(NavGraphBuilder)
    override fun graph() {
        navigation<FinancesNavGraph.Root>(
            startDestination = BudgetBuilderNavGraph.Root,
        ) {
            categoryNavGraph.graph()
            builderNavGraph.graph()
        }
    }
}
