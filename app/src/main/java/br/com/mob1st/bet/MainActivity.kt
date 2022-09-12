package br.com.mob1st.bet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import br.com.mob1st.bet.core.logs.Logger
import br.com.mob1st.bet.core.ui.compose.LocalActivity
import br.com.mob1st.bet.core.ui.ds.atoms.BetTheme
import br.com.mob1st.bet.features.launch.AppNavGraph
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val logger by inject<Logger>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.d("The app has been started")
        setContent {
            CompositionLocalProvider(LocalActivity provides this) {
                BetTheme {
                    AppNavGraph()
                }
            }
        }
    }
}