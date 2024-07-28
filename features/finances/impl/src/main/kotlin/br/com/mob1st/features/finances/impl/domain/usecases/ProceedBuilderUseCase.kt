package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.observability.debug.Debuggable
import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.events.NotEnoughItemsToCompleteEvent

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
     * @see [BuilderNextAction.Step.minimumRequiredToProceed]
     */
    suspend operator fun invoke(builder: BudgetBuilder) {
        val remainingInputs = builder.calculateRemainingInputs()
        if (remainingInputs == 0) {
            if (builder.next is BuilderNextAction.Step) {
                startBuilderStepUseCase(builder.next)
            }
        } else {
            analyticsReporter.log(
                NotEnoughItemsToCompleteEvent(
                    step = builder.id,
                    remainingInputs = remainingInputs,
                ),
            )
            throw NotEnoughInputsException(remainingInputs)
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
