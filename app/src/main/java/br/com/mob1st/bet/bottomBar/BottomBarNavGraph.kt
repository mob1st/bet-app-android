package br.com.mob1st.bet.bottomBar

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.mob1st.bet.screens.HomeScreen
import br.com.mob1st.bet.screens.OverviewScreen
import br.com.mob1st.bet.screens.ProfileScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomBarData.Home.route) {
        composable(route = BottomBarData.Home.route) {
            HomeScreen()
        }
        composable(route = BottomBarData.Overview.route) {
            OverviewScreen()
        }
        composable(route = BottomBarData.Profile.route) {
            ProfileScreen()
        }
    }
}