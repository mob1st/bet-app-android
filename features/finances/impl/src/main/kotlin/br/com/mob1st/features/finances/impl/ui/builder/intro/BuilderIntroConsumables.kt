package br.com.mob1st.features.finances.impl.ui.builder.intro

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import br.com.mob1st.core.design.organisms.snack.SnackbarState
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction

@optics
@Immutable
data class BuilderIntroConsumables(
    val navEvent: NavEvent? = null,
    val snackbar: SnackbarState? = null,
) {
    /**
     * Navigates to the given [step].
     * @param step The step to navigate to.
     * @return The next consumable state.
     */
    fun navigateToStep(step: BudgetBuilderAction.Step) =
        copy(
            navEvent = BuilderIntroNextStepNavEvent(step),
        )

    /**
     * Navigation event triggered from the intro screen
     */
    sealed interface NavEvent

    /**
     * For [optics]
     */
    companion object
}

/**
 * Navigates to the first builder step.
 * @property step The first step to navigate to.
 */
@Immutable
internal data class BuilderIntroNextStepNavEvent(
    val step: BudgetBuilderAction.Step,
) : BuilderIntroConsumables.NavEvent
