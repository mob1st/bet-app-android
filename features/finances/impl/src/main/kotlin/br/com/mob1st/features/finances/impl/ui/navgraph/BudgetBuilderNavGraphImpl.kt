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
import br.com.mob1st.core.androidx.navigation.bottomSheet
import br.com.mob1st.core.androidx.navigation.jsonParcelableType
import br.com.mob1st.core.design.molecules.transitions.PatternKey
import br.com.mob1st.core.design.molecules.transitions.PatternKeyNavType
import br.com.mob1st.core.design.molecules.transitions.route
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroPage
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderNavRoute
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRouter
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderStepNavType
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepPage
import br.com.mob1st.features.finances.impl.ui.category.components.sheet.Args
import br.com.mob1st.features.finances.impl.ui.category.detail.CategoryDetailPage
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
                    onNext = { navEvent -> navController.navigate(router.route(navEvent)) },
                )
            }
            route<BuilderNavRoute.Step>(typeMap = stepTypeMap) { navEntry ->
                BudgetBuilderStepPage(
                    step = router.receive(navEntry.toRoute<BuilderNavRoute.Step>()),
                    onNext = { navEvent -> navController.navigate(router.route(navEvent)) },
                    onBack = navController::navigateUp,
                )
            }
            bottomSheet<BuilderNavRoute.CategoryDetail>(
                typeMap = mapOf(
                    typeOf<Args>() to jsonParcelableType<Args>(),
                    typeOf<PatternKey>() to PatternKeyNavType,
                ),
            ) {
                val route = it.toRoute<BuilderNavRoute.CategoryDetail>()
                val intent = when {
                    route.args.name != null -> GetCategoryIntent.Create(route.args.name)
                    route.args.id != null -> GetCategoryIntent.Edit(Category.Id(route.args.id))
                    else -> error("Invalid route")
                }
                CategoryDetailPage(
                    intent = intent,
                    onSubmit = navController::navigateUp,
                )
            }
            completion {
                navController.navigate(BuilderNavRoute.Intro())
            }
        }
    }

    private companion object {
        private val stepTypeMap = mapOf(typeOf<BuilderNavRoute.Step.Id>() to BuilderStepNavType)
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
