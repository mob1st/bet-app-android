package br.com.mob1st.features.finances.impl.ui.builder.navigation

import br.com.mob1st.core.design.motion.transition.NavTarget
import br.com.mob1st.core.design.motion.transition.TransitionPattern
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import kotlinx.serialization.Serializable

/**
 * All routes for the builder step navigation.
 * New screens should be added here and use the [Serializable] annotation to support type safe navigation.
 */
sealed interface BuilderRoute : NavTarget {
    /**
     * The intro screen for the builder.
     */
    @Serializable
    data object Intro : BuilderRoute {
        override fun pattern(): TransitionPattern = TransitionPattern.TopLevel
    }

    /**
     * All the screen steps during the builder flow.
     * @property type Indicates which step is the specific screen.
     */
    @Serializable
    data class Step(
        val type: Type,
    ) : BuilderRoute {
        /**
         * The type of the step.
         */
        enum class Type {
            FixedExpenses,
            VariableExpenses,
            SeasonalExpenses,
            FixedIncomes,
        }

        override fun pattern(): TransitionPattern = TransitionPattern.Slide
    }

    /**
     * The completion screen for the builder.
     */
    @Serializable
    data object Completion : BuilderRoute {
        override fun pattern(): TransitionPattern = TransitionPattern.Slide
    }

    /**
     * The factory to create the routes.
     */
    interface Factory {
        /**
         * Creates a new route based on the given [action].
         * @param action The next action to be performed.
         * @return The new route.
         */
        fun create(action: BuilderNextAction): BuilderRoute
    }

    /**
     * The parser to create the steps.
     */
    interface StepParser {
        /**
         * Parses the given [route] into a [BuilderNextAction.Step].
         * @param route The route to be parsed.
         * @return The next action to be performed.
         */
        fun parse(route: BuilderRoute): BuilderNextAction.Step
    }
}
