package br.com.mob1st.bet.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import br.com.mob1st.bet.core.ui.compose.LocalLazyListState
import br.com.mob1st.bet.core.ui.compose.LocalSnackbarState
import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry

/**
 * The Scaffold screen of the app, used to display the 3 tabs and provide access to all features
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    entry: CompetitionEntry,
    homeUiState: HomeUiState = rememberHomeUiState(),
) {
    CompositionLocalProvider(LocalSnackbarState provides homeUiState.snackbarHostState) {
        Scaffold(
            bottomBar = {
                if (homeUiState.showBottomBar) {
                    HomeBottomBar(homeUiState)
                }
            },
            snackbarHost = { SnackbarHost(hostState = LocalSnackbarState.current) }
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(it)
            ) {
                CompositionLocalProvider(
                    //TODO add conditional to select the scroll state based on the current tab
                    LocalLazyListState provides homeUiState.competitionsListLazyListState
                ) {
                    HomeNavGraph(homeUiState, entry)
                }
            }
        }
    }
}

@Composable
private fun HomeBottomBar(homeUiState: HomeUiState) {
    val navBackStackEntry by homeUiState.navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar {
        homeUiState.tabs.forEach { tab ->
            BottomTabItem(
                tab = tab,
                currentDestination = currentDestination,
                homeUiState = homeUiState
            )
        }
    }
}

@Composable
private fun RowScope.BottomTabItem(
    tab: BottomBarDestination,
    currentDestination: NavDestination?,
    homeUiState: HomeUiState
) {
    val tabTitle = stringResource(id = tab.title)
    NavigationBarItem(
        label = {
            Text(text = tabTitle)
        },
        icon = {
            Icon(
                imageVector = tab.icon,
                contentDescription = null
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == tab.route
        } == true,
        onClick = {
            homeUiState.navigateTo(tab)
        }
    )
}
