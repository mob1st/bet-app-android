package br.com.mob1st.bet.features.launch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import br.com.mob1st.core.androidx.compose.LocalActivity
import br.com.mob1st.core.design.atoms.theme.TwoCentsTheme
import br.com.mob1st.core.design.atoms.theme.UiContrast
import br.com.mob1st.core.design.utils.LocalLocale
import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.publicapi.domain.ui.BudgetBuilderNavGraph
import br.com.mob1st.features.finances.publicapi.domain.ui.FinancesNavGraph
import br.com.mob1st.features.utils.observability.LocalAnalyticsReporter
import org.koin.android.ext.android.inject
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    private val analyticsReporter by inject<AnalyticsReporter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinContext {
                CompositionLocalProvider(
                    LocalAnalyticsReporter provides analyticsReporter,
                    LocalActivity provides this,
                    LocalLocale provides resources.configuration.locales[0],
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
private fun App() {
    UiContrast {
        TwoCentsTheme {
            Surface {
                NavigationGraph()
            }
        }
    }
}

@Composable
internal fun NavigationGraph() {
    val navController = rememberNavController()
    val financesNavGraph = koinInject<FinancesNavGraph>()
    val budgetBuilderNavGraph = koinInject<BudgetBuilderNavGraph>()
    NavHost(
        navController = navController,
        startDestination = BudgetBuilderNavGraph.Root,
    ) {
        financesNavGraph.graph(
            navController = navController,
            onClickClose = { },
        )
        budgetBuilderNavGraph.graph(
            navController,
            onComplete = {
                // go to home
            },
        )
    }
}
