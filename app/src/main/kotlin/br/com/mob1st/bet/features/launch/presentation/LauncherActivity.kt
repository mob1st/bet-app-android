package br.com.mob1st.bet.features.launch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import br.com.mob1st.core.design.atoms.motion.materialSharedAxisXIn
import br.com.mob1st.core.design.atoms.motion.materialSharedAxisXOut
import br.com.mob1st.core.design.atoms.motion.rememberSlideDistance
import br.com.mob1st.core.design.atoms.theme.TwoCentsTheme
import br.com.mob1st.core.design.atoms.theme.UiContrast
import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.publicapi.domain.ui.BudgetBuilderNavGraph
import br.com.mob1st.features.finances.publicapi.domain.ui.FinancesNavGraph
import br.com.mob1st.features.utils.observability.LocalAnalyticsReporter
import org.koin.android.ext.android.inject
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import timber.log.Timber

class LauncherActivity : ComponentActivity() {
    private val analyticsReporter by inject<AnalyticsReporter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoinContext {
                CompositionLocalProvider(
                    LocalAnalyticsReporter provides analyticsReporter,
                ) {
                    UiContrast {
                        TwoCentsTheme {
                            Surface {
                                NavigationGraph()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun NavigationGraph() {
    Timber.d("ptest NavigationGraph")
    val navController = rememberNavController()
    val financesNavGraph = koinInject<FinancesNavGraph>()
    val budgetBuilderNavGraph = koinInject<BudgetBuilderNavGraph>()
    val slideDistance = rememberSlideDistance()
    NavHost(
        navController = navController,
        // startDestination = "root",
        startDestination = BudgetBuilderNavGraph.Root.toString(),
    ) {
//        financesNavGraph.graph(
//            navController = navController,
//            onClickClose = { },
//        )
        budgetBuilderNavGraph.graph(
            navController,
            slideDistance = slideDistance,
            onComplete = { /* go to home */ },
        )
        navigation(route = "root", startDestination = "a") {
            composable(
                "a",
                exitTransition = {
                    materialSharedAxisXOut(true, slideDistance)
                },
                popEnterTransition = {
                    materialSharedAxisXIn(false, slideDistance)
                },
            ) {
                Scaffold {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                            .padding(it),
                        contentAlignment = Alignment.Center,
                    ) {
                        Button(onClick = { navController.navigate("b") }) {
                            Text(text = "click on me 1")
                        }
                    }
                }
            }
            composable(
                "b",
                enterTransition = {
                    materialSharedAxisXIn(true, slideDistance)
                },
                popExitTransition = {
                    materialSharedAxisXOut(false, slideDistance)
                },
            ) {
                Scaffold {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                            .padding(it),
                        contentAlignment = Alignment.Center,
                    ) {
                        Button(onClick = { navController.popBackStack() }) {
                            Text(text = "click on me 2")
                        }
                    }
                }
            }
        }
    }
}
