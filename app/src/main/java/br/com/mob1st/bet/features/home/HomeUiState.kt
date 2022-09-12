package br.com.mob1st.bet.features.home

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.mob1st.bet.R

/**
 * The source of truth for all UI state related for home
 *
 * It centralizes the states and handle the side-effect actions that can change the state of the
 * scaffold UI
 *
 * ** See also ** [https://developer.android.com/jetpack/compose/state#state-holder-source-of-truth]
 */
class HomeUiState(
    val navController: NavHostController,
    val snackbarHostState: SnackbarHostState,
    private val resources: Resources
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
    var showBottomBar by mutableStateOf(true)

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

    /**
     * Show a snackbar containing the default dismiss action only
     */
    suspend fun showDismissSnackbar(
        @StringRes message: Int,
        onDismiss: () -> Unit
    ) {
        snackbarHostState.showSnackbar(
            resources.getString(message),
            withDismissAction = true
        )
        onDismiss()
    }

    /**
     * Show a snackbar with a retry action
     */
    suspend fun showRetrySnackbar(
        @StringRes message: Int,
        onRetry: () -> Unit,
        onDismiss: () -> Unit,
    ) {
        val result = snackbarHostState.showSnackbar(
            resources.getString(message),
            actionLabel = resources.getString(R.string.retry),
            withDismissAction = true
        )
        when (result) {
            SnackbarResult.Dismissed -> onDismiss()
            SnackbarResult.ActionPerformed -> onRetry()
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
    resources: Resources = LocalContext.current.resources,
): HomeUiState = remember(navController, snackbarHostState, resources) {
    HomeUiState(navController, snackbarHostState, resources)
}
