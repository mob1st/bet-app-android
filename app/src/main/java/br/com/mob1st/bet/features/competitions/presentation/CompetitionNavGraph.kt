package br.com.mob1st.bet.features.competitions.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.mob1st.bet.features.home.BottomBarDestination
import br.com.mob1st.bet.features.home.HomeUiState

/**
 * Implements the nav graph
 */
fun NavGraphBuilder.competitionNavGraph(
    homeUiState: HomeUiState,
    viewModel: ConfrontationListViewModel,
) {
    composable(route = BottomBarDestination.Competitions.route) {
        CompetitionsTabScreen(viewModel) {
            homeUiState.navController.navigate(BottomBarDestination.Competitions.route + "/$it")
        }
    }
    composable(
        route = "competitions/{index}",
        arguments = listOf(navArgument("index") { type = NavType.IntType })
    ) { navBackStackEntry ->
        navBackStackEntry.savedStateHandle
        val index = requireNotNull(navBackStackEntry.arguments).getInt("index")
        SelectedCompetition(viewModel, index) {
            homeUiState.navController.popBackStack()
        }
    }
}