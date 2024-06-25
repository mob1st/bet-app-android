package br.com.mob1st.features.finances.impl.domain.events

import br.com.mob1st.core.observability.events.ScreenViewEvent
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction

internal object BuilderStepScreenViewFactory {
    fun create(
        step: BuilderNextAction.Step,
    ): ScreenViewEvent {
        return when (step) {
            BuilderNextAction.Step.FIXED_EXPENSES -> ScreenViewEvent("builder_fixed_expenses")
            BuilderNextAction.Step.VARIABLE_EXPENSES -> ScreenViewEvent("builder_variable_expenses")
            BuilderNextAction.Step.FIXED_INCOMES -> ScreenViewEvent("builder_fixed_incomes")
        }
    }
}
