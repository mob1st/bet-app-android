package br.com.mob1st.core.observability.events

/**
 * A standard interface for analytics logging.
 *
 * It can be user as a wrapper for third party analytics tools
 */
interface AnalyticsReporter {

    /**
     * Log an event.
     *
     * @param event The event to be logged
     */
    fun log(event: AnalyticsEvent)
}
