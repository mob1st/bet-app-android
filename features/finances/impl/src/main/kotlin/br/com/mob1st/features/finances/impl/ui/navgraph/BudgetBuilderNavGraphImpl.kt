package br.com.mob1st.features.finances.impl.ui.navgraph

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.mob1st.core.design.molecules.transitions.BackAndForward
import br.com.mob1st.core.design.molecules.transitions.TopLevel
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroPage
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRoute
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepPage
import br.com.mob1st.features.finances.publicapi.domain.ui.BudgetBuilderNavGraph
import timber.log.Timber

object BudgetBuilderNavGraphImpl : BudgetBuilderNavGraph {
    context(NavGraphBuilder)
    override fun graph(
        navController: NavController,
        onComplete: () -> Unit,
    ) {
        navigation(
            route = BudgetBuilderNavGraph.Root.toString(),
            startDestination = BuilderRoute.Intro.toString(),
        ) {
            composable(
                route = BuilderRoute.Intro.toString(),
                enterTransition = {
                    TopLevel.enter()
                },
                exitTransition = {
                    BackAndForward(this, SlideDirection.Start).exit()
                },
                popEnterTransition = {
                    BackAndForward(this, SlideDirection.End).enter()
                },
                popExitTransition = {
                    TopLevel.exit()
                },
            ) {
                BuilderIntroPage(
                    onNext = {
                        Timber.d("ptest navigation")
                        navController.navigate("/step")
                    },
                )
            }
            composable(
                route = "/step",
                // typeMap = mapOf(typeOf<BuilderRoute.Step.Type>() to BuilderStepNavType),
                enterTransition = {
                    BackAndForward(this, SlideDirection.Start).enter()
                },
                exitTransition = {
                    TopLevel.exit()
                },
                popExitTransition = {
                    BackAndForward(this, SlideDirection.End).exit()
                },
                popEnterTransition = {
                    TopLevel.enter()
                },
            ) { navBackStackEntry ->
                // val router = koinInject<BuilderRouter>()
                // val route = navBackStackEntry.toRoute<BuilderRoute.Step>()
                // val step = router.from(route)
                BudgetBuilderStepPage(
                    step = VariableExpensesStep,
                    onNext = { navController.navigate(BuilderRoute.Intro.toString()) },
                    onBack = navController::navigateUp,
                )
            }

            composable(
                "c",
                enterTransition = {
                    TopLevel.enter()
                },
                popExitTransition = {
                    TopLevel.exit()
                },
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "Finish")
                    }
                }
            }
        }
    }
}
