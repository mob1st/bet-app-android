package br.com.mob1st.features.finances.impl.ui.builder.navigation

import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroConsumables
import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroNextStepNavEvent
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepCategoryDetailNavEvent
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepConsumables
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNextNavEvent
import br.com.mob1st.features.finances.impl.ui.category.navigation.CategoryCoordinator
import br.com.mob1st.twocents.core.navigation.Coordinator
import br.com.mob1st.twocents.core.navigation.NativeNavigationApi

/**
 * Coordinates the navigation for the budget builder feature flow.
 * @param api The navigation API to use.
 * @property categoryCoordinator Navigates to the category feature.
 */
internal class BuilderCoordinator(
    api: NativeNavigationApi,
    private val categoryCoordinator: CategoryCoordinator,
) : Coordinator<BuilderNavRoute>(api) {
    /**
     * Navigates from the intro screen to the next step.
     * @param event The event that triggered the navigation.
     */
    fun navigate(event: BuilderIntroConsumables.NavEvent) {
        when (event) {
            is BuilderIntroNextStepNavEvent -> {
                navigate(BuilderNavRoute.Step(event.step))
            }
        }
    }

    /**
     * Navigates from the step screen to the next step or the category detail screen.
     * @param event The event that triggered the navigation.
     */
    fun navigate(event: BuilderStepConsumables.NavEvent) {
        when (event) {
            is BuilderStepCategoryDetailNavEvent -> {
                categoryCoordinator.navigate(event.args)
            }

            is BuilderStepNextNavEvent -> {
                navigate(actionRoute(event.next))
            }
        }
    }

    private fun actionRoute(builderAction: BudgetBuilderAction): BuilderNavRoute {
        return when (builderAction) {
            is BudgetBuilderAction.Step -> BuilderNavRoute.Step(builderAction)
            BudgetBuilderAction.Complete -> BuilderNavRoute.Completion()
        }
    }
}
