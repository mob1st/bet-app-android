package br.com.mob1st.bet.features.competitions

import br.com.mob1st.bet.core.analytics.AnalyticsEvent
import br.com.mob1st.bet.core.localization.default
import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry

/**
 * Register when the user subscribes a competition
 */
data class CompetitionSubscribeEvent(
    val entry: CompetitionEntry,
    val method: Method
) : AnalyticsEvent {
    override val name: String
        get() = "bet_competition_subscribe"

    override fun params(): Map<String, Any> {
        return mapOf(
            "id" to entry.id,
            "name" to entry.name.default,
            "type" to entry.type.name.lowercase(),
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
