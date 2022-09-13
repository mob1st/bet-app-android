package br.com.mob1st.bet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import br.com.mob1st.bet.core.analytics.AnalyticsTool
import br.com.mob1st.bet.core.logs.Logger
import br.com.mob1st.bet.core.ui.compose.LocalActivity
import br.com.mob1st.bet.core.ui.compose.LocalAnalyticsTool
import br.com.mob1st.bet.core.ui.compose.LocalLogger
import br.com.mob1st.bet.core.ui.ds.atoms.BetTheme
import br.com.mob1st.bet.features.launch.presentation.AppNavGraph
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val logger by inject<Logger>()
    private val analyticsTool by inject<AnalyticsTool>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.d("The app has been started")
        setContent {
            CompositionLocalProvider(
                LocalActivity provides this,
                LocalAnalyticsTool provides analyticsTool,
                LocalLogger provides logger
            ) {
                BetTheme {
                    AppNavGraph()
                }
            }
        }
    }
}