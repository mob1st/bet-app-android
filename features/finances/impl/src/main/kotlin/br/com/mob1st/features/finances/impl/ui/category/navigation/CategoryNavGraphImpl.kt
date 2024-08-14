package br.com.mob1st.features.finances.impl.ui.category.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import br.com.mob1st.core.androidx.navigation.bottomSheet
import br.com.mob1st.features.finances.impl.ui.category.detail.CategoryDetailPage
import br.com.mob1st.features.finances.publicapi.domain.ui.CategoryNavGraph

/**
 * The navigation graph for the category feature flow.
 * @param coordinator The coordinator of the navigation.
 */
internal class CategoryNavGraphImpl(
    private val coordinator: CategoryCoordinator,
) : CategoryNavGraph {
    context(NavGraphBuilder)
    override fun graph() {
        bottomSheet<CategoryNavRoute.Detail>(
            typeMap = CategoryNavRoute.navTypes,
        ) { entry ->
            val route = entry.toRoute<CategoryNavRoute.Detail>()
            CategoryDetailPage(
                args = route.args,
                isExpense = route.args.isExpense,
                recurrenceType = route.args.recurrenceType,
                onSubmit = coordinator::back,
            )
        }
    }
}
