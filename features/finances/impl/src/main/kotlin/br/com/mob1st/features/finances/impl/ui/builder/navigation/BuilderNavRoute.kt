package br.com.mob1st.features.finances.impl.ui.builder.navigation

import br.com.mob1st.core.design.molecules.transitions.NavRoute
import br.com.mob1st.core.design.molecules.transitions.PatternKey
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
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
     * @property id Indicates which step is the specific screen.
     */
    @Serializable
    data class Step(
        val id: BudgetBuilderAction.Step,
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
            typeOf<BudgetBuilderAction.Step>() to BuilderStepNavType,
        )
    }
}
