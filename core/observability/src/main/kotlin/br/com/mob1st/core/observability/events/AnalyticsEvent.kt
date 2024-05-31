package br.com.mob1st.core.observability.events

import br.com.mob1st.core.observability.debug.Debuggable

/**
 * A standard interface for events that can be tracked by analytics.
 */
interface AnalyticsEvent : Debuggable {
    /**
     * The name of the event.
     */
    val name: String
}
