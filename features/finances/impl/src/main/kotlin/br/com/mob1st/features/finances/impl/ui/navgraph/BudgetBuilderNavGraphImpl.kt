package br.com.mob1st.features.finances.impl.ui.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.mob1st.core.design.atoms.motion.materialSharedAxisXIn
import br.com.mob1st.core.design.atoms.motion.materialSharedAxisXOut
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
        slideDistance: Int,
        onComplete: () -> Unit,
    ) {
        navigation(
            route = BudgetBuilderNavGraph.Root.toString(),
            startDestination = BuilderRoute.Intro.toString(),
        ) {
            composable(
                route = BuilderRoute.Intro.toString(),
                enterTransition = {
                    null
                },
                exitTransition = {
                    materialSharedAxisXOut(true, slideDistance)
                },
                popEnterTransition = {
                    materialSharedAxisXIn(false, slideDistance)
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
                    materialSharedAxisXIn(true, slideDistance)
                },
                popExitTransition = {
                    materialSharedAxisXOut(false, slideDistance)
                },
            ) { navBackStackEntry ->
                // val router = koinInject<BuilderRouter>()
                // val route = navBackStackEntry.toRoute<BuilderRoute.Step>()
                // val step = router.from(route)
                BudgetBuilderStepPage(
                    step = VariableExpensesStep,
                    onNext = {},
                    onBack = navController::navigateUp,
                )
            }
        }
    }
}
