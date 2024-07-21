package br.com.mob1st.features.finances.impl.domain.events

import br.com.mob1st.core.observability.events.ScreenViewEvent
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep

/**
 * Creates the screen event related to a builder step.
 */
internal object BuilderStepScreenViewFactory {
    /**
     * Uses the given [step] to create the screen view event.
     * @param step The step of the builder.
     * @return The screen view event.
     */
    fun create(
        step: BuilderNextAction.Step,
    ): ScreenViewEvent {
        return when (step) {
            FixedExpensesStep -> ScreenViewEvent("builder_fixed_expenses")
            VariableExpensesStep -> ScreenViewEvent("builder_variable_expenses")
            FixedIncomesStep -> ScreenViewEvent("builder_fixed_incomes")
            SeasonalExpensesStep -> ScreenViewEvent("builder_seasonal_expenses")
        }
    }
}
