package br.com.mob1st.bet.features.launch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import br.com.mob1st.bet.core.ui.compose.setThemedContent
import br.com.mob1st.bet.core.ui.ds.molecule.SystemBarProperties
import br.com.mob1st.bet.core.ui.ds.molecule.SystemBars
import br.com.mob1st.bet.features.home.impl.presentation.HomePage

class LauncherActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setThemedContent {
            HomePage()
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
