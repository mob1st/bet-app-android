package br.com.mob1st.bet.features.launch.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import br.com.mob1st.bet.core.tooling.androidx.intent
import br.com.mob1st.bet.core.tooling.androidx.start
import br.com.mob1st.bet.core.ui.compose.setThemedContent
import br.com.mob1st.bet.core.ui.ds.molecule.SystemBarProperties
import br.com.mob1st.bet.core.ui.ds.molecule.SystemBars
import br.com.mob1st.bet.features.home.HomeActivity
import br.com.mob1st.bet.features.home.HomeActivity.Companion.put

class LauncherActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setThemedContent(
            systemBars = { SplashSystemBars() }
        ) {
            SplashScreen(
                onFinish = { entry ->
                    intent<HomeActivity> {
                        put(entry)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }.start()
                }
            )
        }
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
