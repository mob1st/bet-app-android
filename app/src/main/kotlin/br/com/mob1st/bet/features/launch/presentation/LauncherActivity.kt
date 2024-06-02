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
import br.com.mob1st.features.dev.impl.DevSettingsNavRoot
import br.com.mob1st.features.dev.publicapi.presentation.DevSettingsNavTarget
import br.com.mob1st.features.home.impl.ui.HomeNavRoot

class LauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setThemedContent {
            NavigationGraph()
        }
    }
}

@Composable
internal fun NavigationGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = DevSettingsNavTarget.Route,
    ) {
        HomeNavRoot.graph(navController = navController)
        DevSettingsNavRoot.graph(navController = navController)
    }
}

@Composable
fun SplashSystemBars() {
    SystemBars(
        navigationBarProperties =
            SystemBarProperties(
                color = MaterialTheme.colorScheme.primary,
                darkIcons = false,
            ),
    )
}
