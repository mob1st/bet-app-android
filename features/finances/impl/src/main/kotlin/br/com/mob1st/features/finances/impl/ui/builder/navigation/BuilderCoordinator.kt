package br.com.mob1st.features.finances.impl.ui.builder.navigation

import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroConsumables
import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroNextStepNavEvent
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderCompleteNavEvent
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepCategoryDetailNavEvent
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepConsumables
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNextNavEvent
import br.com.mob1st.features.finances.impl.ui.category.navigation.CategoryCoordinator
import br.com.mob1st.twocents.core.navigation.Coordinator
import br.com.mob1st.twocents.core.navigation.NativeNavigationApi
import timber.log.Timber

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
    fun toStep(event: BuilderIntroConsumables.NavEvent) {
        when (event) {
            is BuilderIntroNextStepNavEvent -> navigate(
                BuilderNavRoute.Step(args = event.step),
            )
        }
    }

    /**
     * Navigates from the step screen to the next step or the category detail screen.
     * @param event The event that triggered the navigation.
     */
    fun toNext(event: BuilderStepConsumables.NavEvent) {
        when (event) {
            is BuilderStepCategoryDetailNavEvent -> categoryCoordinator.toDetail(event.args)

            is BuilderStepNextNavEvent -> navigate(
                BuilderNavRoute.Step(
                    args = event.next,
                ),
            )

            BuilderCompleteNavEvent -> navigate(BuilderNavRoute.Completion())
        }
    }

    fun complete() {
        Timber.d("Complete")
    }
}
