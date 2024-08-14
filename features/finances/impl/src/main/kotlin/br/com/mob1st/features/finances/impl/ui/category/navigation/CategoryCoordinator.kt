package br.com.mob1st.features.finances.impl.ui.category.navigation

import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceType
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
     * @param intent The intent to get the category.
     * @param recurrenceType The recurrence type of the category.
     * @param isExpense Whether the category is an expense.
     */
    fun navigate(
        intent: GetCategoryIntent,
        recurrenceType: RecurrenceType,
        isExpense: Boolean,
    ) {
        val route = CategoryNavRoute.Detail(
            args = CategoryNavRoute.Detail.Args(
                intent = intent,
                recurrenceType = recurrenceType,
                isExpense = isExpense,
            ),
        )
        navigate(route)
    }
}
