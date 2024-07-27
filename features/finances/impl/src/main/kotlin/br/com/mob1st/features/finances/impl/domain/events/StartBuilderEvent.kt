package br.com.mob1st.features.finances.impl.domain.events

import br.com.mob1st.core.observability.events.AnalyticsEvent

data object StartBuilderEvent : AnalyticsEvent {
    override val name: String = "builder_started"
    override val logInfo: Map<String, Any?> = emptyMap()
}
