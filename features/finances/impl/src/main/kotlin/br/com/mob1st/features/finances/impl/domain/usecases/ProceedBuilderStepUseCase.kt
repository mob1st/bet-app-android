package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.CategoryBuilder
import br.com.mob1st.features.finances.impl.domain.events.CompleteBuilderEvent
import br.com.mob1st.features.finances.impl.domain.events.NotEnoughItemsToCompleteEvent

class ProceedBuilderStepUseCase(
    private val analyticsReporter: AnalyticsReporter,
) {
    operator fun invoke(builder: CategoryBuilder) = builder.next()
        .onLeft {
            analyticsReporter.log(
                NotEnoughItemsToCompleteEvent(
                    step = builder.id,
                    remainingInputs = it.remainingInputs,
                ),
            )
        }
        .onRight {
            if (it is BuilderNextAction.Complete) {
                analyticsReporter.log(CompleteBuilderEvent)
            }
        }
}
