package br.com.mob1st.features.twocents.builder.impl.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import br.com.mob1st.core.design.motion.transition.target
import br.com.mob1st.features.twocents.builder.publicapi.BuilderNavGraph

/**
 * Builds the navigation graph for the builder feature.
 */
internal object BuilderNavGraphImpl : BuilderNavGraph {
    override val root: BuilderNavGraph.Root = Root

    context(NavGraphBuilder)
    override fun invoke(
        navController: NavController,
        onComplete: () -> Unit,
    ) {
        navigation<Root>(
            startDestination = BuilderNavTarget.Summary,
        ) {
            target<BuilderNavTarget.Summary> {
                BuilderSummaryPage(
                    onClickStart = { navController.navigate(BuilderNavTarget.FixedCosts) },
                    onBackClicked = { navController.navigateUp() },
                )
            }
            target<BuilderNavTarget.FixedCosts> {}
            target<BuilderNavTarget.VariableCosts> { }
            target<BuilderNavTarget.Income> {
                onComplete()
            }
        }
    }

    object Root : BuilderNavGraph.Root
}
