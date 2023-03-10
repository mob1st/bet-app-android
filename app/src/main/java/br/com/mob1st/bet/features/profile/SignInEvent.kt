package br.com.mob1st.bet.features.profile

import br.com.mob1st.bet.core.analytics.AnalyticsEvent
import br.com.mob1st.bet.features.profile.domain.AuthMethod
import com.google.firebase.analytics.FirebaseAnalytics

data class SignInEvent(
    val method: AuthMethod,
) : AnalyticsEvent {
    override val name: String
        get() = FirebaseAnalytics.Event.LOGIN

    override fun params(): Map<String, Any> {
        return mapOf("method" to method.name.lowercase())
    }
}
