package br.com.mob1st.bet.features.launch

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.mob1st.bet.features.home.HomeScreen

@Composable
fun LaunchGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
           LauncherScreen(onFinish = {
               navController.navigate("home")
               navController.popBackStack()
           })
        }
        composable("home") {
            HomeScreen(navController)
        }
    }
}