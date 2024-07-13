package br.com.mob1st.features.finances.impl.ui.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.ui.builder.completion.BuilderCompletionPage
import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroPage
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRoute
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderStepNavType
import br.com.mob1st.features.finances.impl.ui.builder.navigation.builderNextActionIso
import br.com.mob1st.features.finances.impl.ui.builder.navigation.builderStepIso
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepPage
import br.com.mob1st.features.finances.publicapi.domain.ui.CategoryBuilderNavGraph
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

object CategoryBuilderNavGraphImpl : CategoryBuilderNavGraph {
    override val root: CategoryBuilderNavGraph.Root = Root

    context(NavGraphBuilder)
    override fun graph(
        navController: NavController,
        onComplete: () -> Unit,
    ) {
        navigation<Root>(
            startDestination = BuilderRoute.Intro,
        ) {
            composable<BuilderRoute.Intro> {
                BuilderIntroPage(
                    onNext = {
                        val route = builderStepIso.get(BudgetBuilder.firstStep())
                        navController.navigate(route)
                    },
                )
            }
            composable<BuilderRoute.Step>(
                typeMap = mapOf(typeOf<BuilderRoute.Step.Type>() to BuilderStepNavType),
            ) {
                val route = it.toRoute<BuilderRoute.Step>()
                val step = builderStepIso.reverseGet(route)
                BudgetBuilderStepPage(
                    step = step,
                    onNext = { next ->
                        val nextRoute = builderNextActionIso.get(next)
                        navController.navigate(nextRoute)
                    },
                    onBack = navController::navigateUp,
                )
            }
            composable<BuilderRoute.Completion> {
                BuilderCompletionPage(
                    onComplete = onComplete,
                )
            }
        }
    }

    @Serializable
    object Root : CategoryBuilderNavGraph.Root
}
