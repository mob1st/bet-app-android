package br.com.mob1st.libs.firebase.analytics

import android.os.Bundle
import androidx.core.os.bundleOf
import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.core.observability.events.AnalyticsReporter
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Analytics tool implementation for Firebase.
 */
class FirebaseAnalyticsTool(private val firebaseAnalytics: FirebaseAnalytics) : AnalyticsReporter {

    override fun log(event: AnalyticsEvent) {
        firebaseAnalytics.logEvent(event.name, event.logInfo.toBundle())
    }
}

@Suppress("SpreadOperator")
private fun Map<String, Any?>.toBundle(): Bundle = bundleOf(*this.toList().toTypedArray())
