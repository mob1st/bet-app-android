package br.com.mob1st.features.finances.impl.ui.builder.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import androidx.navigation.toRoute
import br.com.mob1st.core.design.molecules.transitions.route
import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroPage
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepPage
import br.com.mob1st.features.finances.publicapi.domain.ui.BudgetBuilderNavGraph

internal class BudgetBuilderNavGraphImpl(
    private val coordinator: BuilderCoordinator,
) : BudgetBuilderNavGraph {
    context(NavGraphBuilder)
    override fun graph() {
        navigation<BudgetBuilderNavGraph.Root>(
            startDestination = BuilderNavRoute.Intro(),
        ) {
            route<BuilderNavRoute.Intro> {
                BuilderIntroPage(onNext = coordinator::navigate)
            }
            route<BuilderNavRoute.Step>(
                typeMap = BuilderNavRoute.navType,
            ) { navEntry ->
                BudgetBuilderStepPage(
                    step = navEntry.toRoute<BuilderNavRoute.Step>().id,
                    onNext = coordinator::navigate,
                    onBack = coordinator::back,
                )
            }
            completion {
            }
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
