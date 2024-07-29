package br.com.mob1st.features.finances.impl.ui.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import br.com.mob1st.features.finances.impl.ui.builder.completion.BuilderCompletionPage
import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroPage
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRoute
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRouter
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderStepNavType
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepPage
import br.com.mob1st.features.finances.publicapi.domain.ui.BudgetBuilderNavGraph
import org.koin.compose.koinInject
import kotlin.reflect.typeOf

object BudgetBuilderNavGraphImpl : BudgetBuilderNavGraph {
    context(NavGraphBuilder)
    override fun graph(
        navController: NavController,
        onComplete: () -> Unit,
    ) {
        navigation<BudgetBuilderNavGraph.Root>(
            startDestination = BuilderRoute.Intro,
        ) {
            composable<BuilderRoute.Intro> {
                BuilderIntroPage(onNext = navController::navigate)
            }
            composable<BuilderRoute.Step>(
                typeMap = mapOf(typeOf<BuilderRoute.Step.Type>() to BuilderStepNavType),
            ) { navBackStackEntry ->
                val router = koinInject<BuilderRouter>()
                val route = navBackStackEntry.toRoute<BuilderRoute.Step>()
                val step = router.from(route)
                BudgetBuilderStepPage(
                    step = step,
                    onNext = navController::navigate,
                    onBack = navController::navigateUp,
                )
            }
            composable<BuilderRoute.Completion> {
                BuilderCompletionPage(onComplete = onComplete)
            }
        }
    }
}
