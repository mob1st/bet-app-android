package br.com.mob1st.features.finances.impl.ui.builder.navigation

import androidx.navigation.NavType
import br.com.mob1st.core.design.molecules.transitions.PatternKey
import br.com.mob1st.core.design.molecules.transitions.TransitionedRoute
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNavArgs
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

/**
 * All routes for the builder step navigation.
 * New screens should be added here and use the [Serializable] annotation to support type safe navigation.
 */
internal sealed interface BuilderNavRoute {
    /**
     * The intro screen for the builder.
     */
    @Serializable
    data class Intro(
        override val enteringPatternKey: PatternKey = PatternKey.TopLevel,
    ) : BuilderNavRoute, TransitionedRoute

    /**
     * All the screen steps during the builder flow.
     * @property args The arguments to pass to the step
     */
    @Serializable
    data class Step(
        val args: BuilderStepNavArgs,
        override val enteringPatternKey: PatternKey = PatternKey.BackAndForward,
    ) : BuilderNavRoute, TransitionedRoute

    /**
     * The completion screen for the builder.
     */
    @Serializable
    data class Completion(
        override val enteringPatternKey: PatternKey = PatternKey.BackAndForward,
    ) : BuilderNavRoute, TransitionedRoute

    companion object {
        val navType = mapOf(
            typeOf<BuilderStepNavArgs>() to NavType.EnumType(BuilderStepNavArgs::class.java),
        )
    }
}
