package br.com.mob1st.bet.features.competitions

import br.com.mob1st.bet.core.analytics.AnalyticsEvent

/**
 * Register when the user subscribes a competition
 */
data class CompetitionSubscriptionEvent(
    val competitionId: String,
    val competitionName: String,
    val method: Method,
) : AnalyticsEvent {
    override val name: String
        get() = "bet_competition_subscription"

    override fun params(): Map<String, Any> {
        return mapOf(
            "competitionId" to competitionId,
            "competitionName" to competitionName,
            "method" to method.name.lowercase()
        )
    }

    /**
     * Indicates how the user subscribed into a competition
     */
    enum class Method {
        /**
         * The user was automatically subscribed to a competition
         */
        AUTOMATIC,

        /**
         * The user joined a group of friends related to a competition
         */
        BY_GROUP,

        /**
         * The user searched and subscribed to a competition
         */
        INTENTIONAL
    }
}