package br.com.mob1st.features.finances.impl.ui.builder.navigation

import br.com.mob1st.core.design.motion.transition.NavTarget
import br.com.mob1st.core.design.motion.transition.TransitionPattern
import kotlinx.serialization.Serializable

/**
 * All routes for the builder step navigation.
 * New screens should be added here and use the [Serializable] annotation to support type safe navigation.
 */
internal sealed interface BuilderRoute : NavTarget {
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
}
