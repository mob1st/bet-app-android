package br.com.mob1st.features.finances.impl.domain.events

import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction

data class NotEnoughItemsToCompleteEvent(
    private val step: BuilderNextAction.Step,
    private val remainingInputs: Int,
) : AnalyticsEvent {
    override val name: String = "not_enough_items_to_complete"
    override val logInfo: Map<String, Any?> = mapOf(
        "step" to step,
        "remainingItems" to remainingInputs,
    )
}
