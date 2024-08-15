package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.observability.debug.Debuggable
import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep

/**
 * Enables the user to proceed in the budget builder until its completion.
 */
internal class ProceedBuilderUseCase(
    private val startBuilderStepUseCase: StartBuilderStepUseCase,
    private val analyticsReporter: AnalyticsReporter,
) {
    /**
     * Proceeds to the next action in the budget builder.
     * It prepares the data structure for the next step if necessary.
     * If not enough category inputs was added, it throws a [NotEnoughInputsException].
     * @param builder The current state of the budget builder.
     * @throws NotEnoughInputsException If there are not enough inputs to proceed to the next step.
     * @see [BudgetBuilderAction.Step.minimumRequiredToProceed]
     */
    suspend operator fun invoke(builder: BudgetBuilder): BudgetBuilderAction {
        val remainingInputs = builder.calculateRemainingInputs()
        if (remainingInputs == 0) {
            val next = builder.next()
            if (next is BudgetBuilderAction.Step) {
                startBuilderStepUseCase(next)
            }
            return next
        } else {
            analyticsReporter.report(
                AnalyticsEvent.notEnoughItemsToComplete(
                    step = builder.id,
                    remainingInputs = remainingInputs,
                ),
            )
            throw NotEnoughInputsException(remainingInputs)
        }
    }

    /**
     * Track the event when the user tries to complete a step but there are not enough items to complete it.
     * @param step The step that the user is trying to complete.
     * @param remainingInputs The number of remaining items to complete the step.
     * @return The event to be tracked.
     */
    private fun AnalyticsEvent.Companion.notEnoughItemsToComplete(
        step: BudgetBuilderAction.Step,
        remainingInputs: Int,
    ) = AnalyticsEvent(
        "not_enough_items_to_complete",
        mapOf(
            "step" to step,
            "remainingItems" to remainingInputs,
        ),
    )

    private fun BudgetBuilder.next(): BudgetBuilderAction {
        return when (id) {
            FixedExpensesStep -> SeasonalExpensesStep
            FixedIncomesStep -> BudgetBuilderAction.Complete
            SeasonalExpensesStep -> FixedIncomesStep
            VariableExpensesStep -> FixedExpensesStep
        }
    }

    /**
     * Represents an error that occurs when the user tries to move to the next step in the category builder but there
     * are not enough inputs yet.
     * @property remainingInputs The number of missing inputs to proceed to the next step.
     */
    class NotEnoughInputsException(
        val remainingInputs: Int,
    ) : Exception("Not enough inputs to proceed to the next step. remainingInputs=$remainingInputs"), Debuggable {
        override val logInfo: Map<String, Any?> = mapOf(
            "remainingInputs" to remainingInputs,
        )
    }
}
