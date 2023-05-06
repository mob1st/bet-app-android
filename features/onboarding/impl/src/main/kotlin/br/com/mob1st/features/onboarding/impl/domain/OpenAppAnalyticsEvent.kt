package br.com.mob1st.features.onboarding.impl.domain

import br.com.mob1st.core.observability.events.AnalyticsEvent
import com.google.firebase.analytics.FirebaseAnalytics

class OpenAppAnalyticsEvent : AnalyticsEvent {
    override val name: String = FirebaseAnalytics.Event.APP_OPEN
    override val logInfo: Map<String, Any?> = emptyMap()
}
