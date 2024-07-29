package br.com.mob1st.core.observability.events

/**
 * A standard interface for events that can be tracked by analytics.
 * @property name The name of the event.
 * @property params The properties to be logged. It's recommended to use primitives and avoid complex structures.
 */
data class AnalyticsEvent(
    val name: String,
    val params: Map<String, Any?> = emptyMap(),
) {
    companion object {
        /**
         * Creates a screen view event, using the standard name event name and property.
         * @param screenName The name of the screen being viewed. It will be used as the value of the "screen_name"
         * property, the default property for screen view events.
         * @param properties The additional properties to be logged. It is not needed to
         */
        fun screenView(
            screenName: String,
            properties: Map<String, Any?> = emptyMap(),
        ) = AnalyticsEvent(
            name = "screen_view",
            params = mapOf("screen_name" to screenName) + properties,
        )
    }
}
