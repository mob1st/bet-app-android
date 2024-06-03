package br.com.mob1st.bet.features.launch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import br.com.mob1st.bet.core.ui.ds.atoms.BetTheme
import br.com.mob1st.features.dev.impl.DevSettingsNavRoot
import br.com.mob1st.features.dev.publicapi.presentation.DevSettingsNavTarget

class LauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BetTheme {
                NavigationGraph(this)
            }
        }
    }
}

@Composable
internal fun NavigationGraph(activity: ComponentActivity) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = DevSettingsNavTarget.Route,
    ) {
        DevSettingsNavRoot.graph(
            navController = navController,
            onBack = { activity.finish() },
        )
    }
}
