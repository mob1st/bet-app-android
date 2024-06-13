package br.com.mob1st.features.twocents.builder.impl.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import br.com.mob1st.core.design.motion.transition.target
import br.com.mob1st.features.twocents.builder.impl.ui.fixed.FixedExpensesPage
import br.com.mob1st.features.twocents.builder.impl.ui.summary.BuilderSummaryPage
import br.com.mob1st.features.twocents.builder.publicapi.BuilderNavGraph
import kotlinx.serialization.Serializable

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
                    onClickBack = navController::navigateUp,
                )
            }
            target<BuilderNavTarget.FixedCosts> {
                FixedExpensesPage(
                    onNext = { navController.navigate(BuilderNavTarget.VariableCosts) },
                    onClickBack = navController::navigateUp,
                )
            }
            target<BuilderNavTarget.VariableCosts> { }
            target<BuilderNavTarget.Income> {
                onComplete()
            }
        }
    }

    @Serializable
    object Root : BuilderNavGraph.Root
}
