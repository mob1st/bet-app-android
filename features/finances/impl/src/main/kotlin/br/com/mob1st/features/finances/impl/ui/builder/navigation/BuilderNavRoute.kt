package br.com.mob1st.features.finances.impl.ui.builder.navigation

import br.com.mob1st.core.design.molecules.transitions.NavRoute
import br.com.mob1st.core.design.molecules.transitions.PatternKey
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.ui.category.components.sheet.Args
import kotlinx.serialization.Serializable

/**
 * All routes for the builder step navigation.
 * New screens should be added here and use the [Serializable] annotation to support type safe navigation.
 */
sealed interface BuilderNavRoute : NavRoute {
    /**
     * The intro screen for the builder.
     */
    @Serializable
    data class Intro(
        override val enteringPatternKey: PatternKey = PatternKey.TopLevel,
    ) : BuilderNavRoute

    /**
     * All the screen steps during the builder flow.
     * @property id Indicates which step is the specific screen.
     */
    @Serializable
    data class Step(
        val id: Id,
        override val enteringPatternKey: PatternKey = PatternKey.BackAndForward,
    ) : BuilderNavRoute {
        enum class Id {
            FixedExpenses,
            VariableExpenses,
            SeasonalExpenses,
            FixedIncomes,
        }
    }

    /**
     * The completion screen for the builder.
     */
    @Serializable
    data class Completion(
        override val enteringPatternKey: PatternKey = PatternKey.BottomSheet,
    ) : BuilderNavRoute

    @Serializable
    data class CategoryDetail(
        override val enteringPatternKey: PatternKey = PatternKey.BottomSheet,
        val args: Args,
    ) : BuilderNavRoute

    /**
     * The factory to create the routes.
     */
    interface Factory {
        /**
         * Creates a new route based on the given [action].
         * @param action The next action to be performed.
         * @return The new route.
         */
        fun create(action: BudgetBuilderAction): BuilderNavRoute
    }
}
