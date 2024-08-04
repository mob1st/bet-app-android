package br.com.mob1st.features.finances.impl.ui.navgraph

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import br.com.mob1st.core.design.molecules.transitions.BackAndForward
import br.com.mob1st.core.design.molecules.transitions.TopLevel
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroPage
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderNavRoute
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRouter
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderStepNavType
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepPage
import br.com.mob1st.features.finances.publicapi.domain.ui.BudgetBuilderNavGraph
import org.koin.compose.koinInject
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
            startDestination = BuilderNavRoute.Intro,
        ) {
            intro {
                val route = router.to(it)
                navController.navigate(route)
            }
            step(
                onNext = {
                    val route = router.to(it)
                    navController.navigate(route)
                },
                onBack = navController::navigateUp,
            )
            completion {
                navController.navigate(BuilderNavRoute.Intro)
            }
        }
    }
}

private fun NavGraphBuilder.intro(
    onNext: (BuilderNextAction.Step) -> Unit,
) {
    composable<BuilderNavRoute.Intro>(
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
        BuilderIntroPage(onNext = onNext)
    }
}

private fun NavGraphBuilder.step(
    onNext: (BuilderNextAction) -> Unit,
    onBack: () -> Unit,
) {
    composable<BuilderNavRoute.Step>(
        typeMap = mapOf(typeOf<BuilderNavRoute.Step.Id>() to BuilderStepNavType),
        enterTransition = {
            BackAndForward(this, SlideDirection.Start).enter()
        },
        exitTransition = {
            BackAndForward(this, SlideDirection.Start).exit()
        },
        popExitTransition = {
            BackAndForward(this, SlideDirection.End).exit()
        },
        popEnterTransition = {
            BackAndForward(this, SlideDirection.End).enter()
        },
    ) { navBackStackEntry ->
        val router = koinInject<BuilderRouter>()
        val route = navBackStackEntry.toRoute<BuilderNavRoute.Step>()
        val step = router.from(route)
        BudgetBuilderStepPage(
            step = step,
            onNext = onNext,
            onBack = onBack,
        )
    }
}

private fun NavGraphBuilder.completion(
    onComplete: () -> Unit,
) {
    composable<BuilderNavRoute.Completion>(
        enterTransition = {
            BackAndForward(this, SlideDirection.Start).enter()
        },
        exitTransition = {
            TopLevel.exit()
        },
        popEnterTransition = {
            TopLevel.enter()
        },
        popExitTransition = {
            BackAndForward(this, SlideDirection.End).exit()
        },
    ) {
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
