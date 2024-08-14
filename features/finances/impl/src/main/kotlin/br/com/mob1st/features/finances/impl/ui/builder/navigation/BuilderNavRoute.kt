package br.com.mob1st.features.finances.impl.ui.builder.navigation

import androidx.navigation.NavType
import br.com.mob1st.core.design.molecules.transitions.NavRoute
import br.com.mob1st.core.design.molecules.transitions.PatternKey
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

/**
 * All routes for the builder step navigation.
 * New screens should be added here and use the [Serializable] annotation to support type safe navigation.
 */
internal sealed interface BuilderNavRoute : NavRoute {
    /**
     * The intro screen for the builder.
     */
    @Serializable
    data class Intro(
        override val enteringPatternKey: PatternKey = PatternKey.TopLevel,
    ) : BuilderNavRoute

    /**
     * All the screen steps during the builder flow.
     * @property args The arguments to pass to the step
     */
    @Serializable
    data class Step(
        val args: BuilderStepNavArgs,
        override val enteringPatternKey: PatternKey = PatternKey.BackAndForward,
    ) : BuilderNavRoute

    /**
     * The completion screen for the builder.
     */
    @Serializable
    data class Completion(
        override val enteringPatternKey: PatternKey = PatternKey.BackAndForward,
    ) : BuilderNavRoute

    companion object {
        val navType = mapOf(
            typeOf<BuilderStepNavArgs>() to NavType.EnumType(BuilderStepNavArgs::class.java),
        )
    }
}
