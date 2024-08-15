package br.com.mob1st.features.finances.impl.ui.builder.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import androidx.navigation.toRoute
import br.com.mob1st.core.design.molecules.transitions.transitioned
import br.com.mob1st.features.finances.impl.ui.builder.completion.BuilderCompletionPage
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
            transitioned<BuilderNavRoute.Intro> {
                BuilderIntroPage(onNext = coordinator::toStep)
            }
            transitioned<BuilderNavRoute.Step>(
                typeMap = BuilderNavRoute.navType,
            ) { navEntry ->
                BudgetBuilderStepPage(
                    args = navEntry.toRoute<BuilderNavRoute.Step>().args,
                    onNext = coordinator::toNext,
                    onBack = coordinator::back,
                )
            }
            transitioned<BuilderNavRoute.Completion> {
                BuilderCompletionPage(
                    onComplete = coordinator::complete,
                )
            }
        }
    }
}
