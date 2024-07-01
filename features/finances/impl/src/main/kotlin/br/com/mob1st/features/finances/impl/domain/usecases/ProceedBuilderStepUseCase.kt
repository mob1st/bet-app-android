package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.CategoryBuilder
import br.com.mob1st.features.finances.impl.domain.entities.NotEnoughInputsException
import br.com.mob1st.features.finances.impl.domain.events.NotEnoughItemsToCompleteEvent
import kotlinx.coroutines.delay

/**
 * Proceeds to the next step of the builder.
 * @param analyticsReporter The analytics reporter. It tracks events in case of not enough inputs.
 */
class ProceedBuilderStepUseCase(
    private val analyticsReporter: AnalyticsReporter,
) {
    /**
     * Proceeds to the next step of the builder.
     * @param builder The current state of the category builder.
     * @return The next action that the user can take.
     * @throws NotEnoughInputsException If there are not enough inputs to proceed to the next step.
     */
    suspend operator fun invoke(builder: CategoryBuilder): BuilderNextAction {
        return try {
            delay(DELAY)
            builder.next()
        } catch (e: NotEnoughInputsException) {
            // Log the exception as an event instead of an error
            analyticsReporter.log(NotEnoughItemsToCompleteEvent(builder.id, e.remainingInputs))
            throw e
        }
    }

    companion object {
        private const val DELAY = 150L
    }
}
