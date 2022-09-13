package br.com.mob1st.bet.features.home

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.mob1st.bet.R
import br.com.mob1st.bet.features.competitions.presentation.CompetitionsTabScreen
import br.com.mob1st.bet.features.groups.GroupsTabScreen
import br.com.mob1st.bet.features.profile.presentation.ProfileTabScreen

/**
 * The possible destination of the bottom bar on home screen
 */
sealed class BottomBarDestination(
    val route: String,
    @StringRes val title: Int,
    val icon: ImageVector
) {
    /**
     * List of competitions and entry point for bet domain
     */
    object Competitions : BottomBarDestination(
        route = "competitions",
        title = R.string.tab_title_competitions,
        icon = Icons.Default.Home
    )

    /**
     * List of groups and entry point for groups domain
     */
    object Groups : BottomBarDestination(
        route = "groups",
        title = R.string.tab_title_groups,
        icon = Icons.Default.Star
    )

    /**
     * Logged user profile and entry point for the user domain
     */
    object Profile : BottomBarDestination(
        route = "profile",
        title = R.string.tab_title_profile,
        icon = Icons.Default.Person
    )
}


@Composable
fun HomeNavGraph(homeUiState: HomeUiState) {
    NavHost(
        navController = homeUiState.navController,
        startDestination = BottomBarDestination.Competitions.route,
    ) {
        composable(route = BottomBarDestination.Competitions.route) {
            CompetitionsTabScreen()
        }
        composable(route = BottomBarDestination.Groups.route) {
            GroupsTabScreen()
        }
        composable(route = BottomBarDestination.Profile.route) {
            ProfileTabScreen()
        }
    }
}