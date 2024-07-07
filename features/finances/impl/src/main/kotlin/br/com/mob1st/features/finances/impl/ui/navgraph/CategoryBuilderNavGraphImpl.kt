package br.com.mob1st.features.finances.impl.ui.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.ui.builder.CategoryBuilderRoute
import br.com.mob1st.features.finances.impl.ui.builder.completion.BuilderCompletionPage
import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroPage
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepPage
import br.com.mob1st.features.finances.publicapi.domain.ui.CategoryBuilderNavGraph

object CategoryBuilderNavGraphImpl : CategoryBuilderNavGraph {
    override val root: CategoryBuilderNavGraph.Root = Root

    context(NavGraphBuilder)
    override fun graph(
        navController: NavController,
        onComplete: () -> Unit,
    ) {
        navigation<Root>(
            startDestination = CategoryBuilderRoute.Intro,
        ) {
            composable<CategoryBuilderRoute.Intro> {
                BuilderIntroPage(
                    onNext = {
                        val route = CategoryBuilderRoute.Step(BudgetBuilder.firstStep())
                        navController.navigate(route)
                    },
                )
            }
            composable<CategoryBuilderRoute.Step> {
                val step = it.toRoute<CategoryBuilderRoute.Step>()
                BudgetBuilderStepPage(
                    step = step.args,
                    onNext = { next ->
                        val route = CategoryBuilderRoute.from(next)
                        navController.navigate(route)
                    },
                    onBack = navController::navigateUp,
                )
            }
            composable<CategoryBuilderRoute.BuilderCompletion> {
                BuilderCompletionPage(
                    onComplete = onComplete,
                )
            }
        }
    }

    object Root : CategoryBuilderNavGraph.Root
}
