package br.com.mob1st.bet.features.launch

import br.com.mob1st.bet.core.analytics.AnalyticsEvent
import com.google.firebase.analytics.FirebaseAnalytics

class AnonymousSignInEvent : AnalyticsEvent {
    override val name: String
        get() = FirebaseAnalytics.Event.LOGIN

    override fun params(): Map<String, Any> {
        return mapOf("method" to "Anonymous")
    }
}
