package br.com.mob1st.bet.screens

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.mob1st.bet.features.competitions.presentation.CompetitionListPage
import br.com.mob1st.bet.features.competitions.presentation.GetCompetitionEvent
import br.com.mob1st.bet.features.competitions.presentation.GetCompetitionViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {
        BottomNavGraph(navController = navController)
    }
}

@Composable
fun BottomNavGraph(navController: NavHostController) {
    val getCompetitionViewModel = koinViewModel<GetCompetitionViewModel>()
    NavHost(navController, startDestination = BottomBarData.Home.route) {
        composable(route = BottomBarData.Home.route) {
            val state by getCompetitionViewModel.uiState.collectAsState()
            CompetitionListPage(
                state = state,
                onTryAgain = { getCompetitionViewModel.fromUi(GetCompetitionEvent.TryAgain(it)) },
                onClickToCreate = { getCompetitionViewModel.fromUi(GetCompetitionEvent.Create) },
            )
        }
        composable(route = BottomBarData.Overview.route) {
            OverviewScreen()
        }
        composable(route = BottomBarData.Profile.route) {
            ProfileScreen()
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val tabs = listOf(
        BottomBarData.Home,
        BottomBarData.Overview,
        BottomBarData.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        tabs.forEach { tab ->
            AddItem(
                tab = tab,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    tab: BottomBarData,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        label = {
            Text(text = tab.title)
        },
        icon = {
            Icon(
                imageVector = tab.icon,
                contentDescription = "Navigation icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == tab.route
        } == true,
        onClick = {
            navController.navigate(tab.route)
        }
    )
}
