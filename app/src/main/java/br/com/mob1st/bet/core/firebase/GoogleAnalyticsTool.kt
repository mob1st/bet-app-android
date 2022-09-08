package br.com.mob1st.bet.core.firebase

import androidx.core.os.bundleOf
import br.com.mob1st.bet.core.analytics.AnalyticsEvent
import br.com.mob1st.bet.core.analytics.AnalyticsTool
import com.google.firebase.analytics.FirebaseAnalytics

class GoogleAnalyticsTool(
    private val analytics: FirebaseAnalytics
) : AnalyticsTool {

    override fun registerUser(userId: String) {
        analytics.setUserId(userId)
    }

    override fun clearUser() {
        analytics.setUserId(null)
    }

    override fun log(event: AnalyticsEvent) {
        val params = event.params()
        val bundle = if (params.isNotEmpty()) {
            bundleOf(*params.toList().toTypedArray())
        } else null
        analytics.logEvent(event.name, bundle)
    }
}