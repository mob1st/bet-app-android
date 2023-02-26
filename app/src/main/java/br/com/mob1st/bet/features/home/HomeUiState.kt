package br.com.mob1st.bet.features.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

/**
 * The source of truth for all UI state related for home
 *
 * It centralizes the states and handle the side-effect actions that can change the state of the
 * scaffold UI
 *
 * ** See also ** [https://developer.android.com/jetpack/compose/state#state-holder-source-of-truth]
 */
@Stable
class HomeUiState(
    val navController: NavHostController,
    val snackbarHostState: SnackbarHostState,
    val competitionsListLazyListState: LazyListState
) {

    /**
     * The tabs available in home
     */
    val tabs = listOf(
        BottomBarDestination.Competitions,
        BottomBarDestination.Groups,
        BottomBarDestination.Profile
    )

    // TODO implement custom logic to hide bottom bar depending on the screan
    val showBottomBar @Composable get() = navController
        .currentBackStackEntryAsState()
        .value
        ?.destination
        ?.route in tabs.map { it.route }

    /**
     * Navigates to one of the tabs in the app
     */
    fun navigateTo(tab: BottomBarDestination) {
        navController.navigate(tab.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}

/**
 * Creates the instance of [HomeUiState]
 */
@Composable
fun rememberHomeUiState(
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    lazyListState: LazyListState = rememberLazyListState()
): HomeUiState = remember(navController, snackbarHostState) {
    HomeUiState(navController, snackbarHostState, lazyListState)
}
