package br.com.mob1st.bet.features.launch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import br.com.mob1st.bet.core.ui.compose.setThemedContent
import br.com.mob1st.bet.core.ui.ds.molecule.SystemBarProperties
import br.com.mob1st.bet.core.ui.ds.molecule.SystemBars
import br.com.mob1st.features.home.impl.ui.HomeNavRoot
import br.com.mob1st.features.home.publicapi.ui.HomeNavTarget

class LauncherActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setThemedContent {
            NavigationGraph()
        }
    }
}

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = HomeNavTarget.ROUTE) {
        HomeNavRoot.graph(navController = navController)
    }
}

@Composable
fun SplashSystemBars() {
    SystemBars(
        navigationBarProperties = SystemBarProperties(
            color = MaterialTheme.colorScheme.primary,
            darkIcons = false
        )
    )
}
