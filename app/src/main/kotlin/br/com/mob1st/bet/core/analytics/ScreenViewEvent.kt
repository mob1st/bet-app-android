package br.com.mob1st.bet.core.analytics

import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Register the visibility of one screen
 */
data class ScreenViewEvent(
    val screenName: String,
    val params: Map<String, Any> = emptyMap(),
) : AnalyticsEvent {
    override val name: String
        get() = FirebaseAnalytics.Event.SCREEN_VIEW

    override fun params(): Map<String, Any> {
        return mapOf(FirebaseAnalytics.Param.SCREEN_NAME to screenName) + params
    }
}

/**
 * Register the dismiss of one screen
 */
data class ScreenDismissEvent(
    val screenName: String,
    val params: Map<String, Any> = emptyMap(),
) : AnalyticsEvent {
    override val name: String
        get() = FirebaseAnalytics.Event.SCREEN_VIEW

    override fun params(): Map<String, Any> {
        return mapOf(FirebaseAnalytics.Param.SCREEN_NAME to screenName) + params
    }
}
