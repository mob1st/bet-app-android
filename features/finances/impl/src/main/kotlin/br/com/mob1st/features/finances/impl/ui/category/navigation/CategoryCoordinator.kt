package br.com.mob1st.features.finances.impl.ui.category.navigation

import br.com.mob1st.features.finances.impl.ui.category.detail.CategoryDetailArgs
import br.com.mob1st.twocents.core.navigation.Coordinator
import br.com.mob1st.twocents.core.navigation.NativeNavigationApi

/**
 * Coordinates the navigation for the category feature flow.
 * @param navigationApi The navigation API to use.
 */
internal class CategoryCoordinator(
    navigationApi: NativeNavigationApi,
) : Coordinator<CategoryNavRoute>(navigationApi) {
    /**
     * Navigates to the category detail screen.
     * @param args The arguments to pass to the detail screen.
     */
    fun navigate(args: CategoryDetailArgs) {
        val route = CategoryNavRoute.Detail(args = args)
        navigate(route)
    }
}
