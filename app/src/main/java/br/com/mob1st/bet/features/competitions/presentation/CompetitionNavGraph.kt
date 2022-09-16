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
            homeUiState.navController.navigate("createGuess")
        }
    }
    composable(
        route = "createGuess"
    ) {
        SelectedCompetition(viewModel) {
            homeUiState.navController.popBackStack()
        }
    }
}