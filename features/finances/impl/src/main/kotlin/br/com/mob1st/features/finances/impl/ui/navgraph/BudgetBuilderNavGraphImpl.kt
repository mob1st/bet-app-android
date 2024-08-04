package br.com.mob1st.features.finances.impl.ui.navgraph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import androidx.navigation.toRoute
import br.com.mob1st.core.design.molecules.transitions.route
import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroPage
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderNavRoute
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRouter
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderStepNavType
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepPage
import br.com.mob1st.features.finances.publicapi.domain.ui.BudgetBuilderNavGraph
import kotlin.reflect.typeOf

internal class BudgetBuilderNavGraphImpl(
    private val router: BuilderRouter,
) : BudgetBuilderNavGraph {
    context(NavGraphBuilder)
    override fun graph(
        navController: NavController,
        onComplete: () -> Unit,
    ) {
        navigation<BudgetBuilderNavGraph.Root>(
            startDestination = BuilderNavRoute.Intro(),
        ) {
            route<BuilderNavRoute.Intro> {
                BuilderIntroPage(
                    onNext = {
                        val route = router.to(it)
                        navController.navigate(route)
                    },
                )
            }
            route<BuilderNavRoute.Step>(
                typeMap = mapOf(typeOf<BuilderNavRoute.Step.Id>() to BuilderStepNavType),
            ) {
                val route = it.toRoute<BuilderNavRoute.Step>()
                BudgetBuilderStepPage(
                    step = router.from(route),
                    onNext = { action ->
                        navController.navigate(router.to(action))
                    },
                    onBack = navController::navigateUp,
                )
            }
        }
        completion {
            navController.navigate(BuilderNavRoute.Intro())
        }
    }
}

private fun NavGraphBuilder.completion(
    onComplete: () -> Unit,
) {
    route<BuilderNavRoute.Completion> {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Button(onClick = onComplete) {
                    Text(text = "Finish")
                }
            }
        }
    }
}
