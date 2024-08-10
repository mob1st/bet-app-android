package br.com.mob1st.features.finances.impl.ui.category.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.mob1st.core.design.molecules.transitions.route
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.ui.category.detail.CategoryDetailPage

@Composable
fun CategoryNavHost(
    navController: NavHostController,
    intent: GetCategoryIntent,
    onSubmit: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = "sss",
    ) {
        route<CategoryNavRoute.Detail> {
            CategoryDetailPage(
                intent = intent,
                onSubmit = onSubmit,
            )
        }
    }
}
