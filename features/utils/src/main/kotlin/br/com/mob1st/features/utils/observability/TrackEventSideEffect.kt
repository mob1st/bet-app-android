package br.com.mob1st.features.utils.observability

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import br.com.mob1st.core.observability.events.AnalyticsEvent

/**
 * Allows event tracking inside a composable context.
 * It wraps the event report into a side effect to avoid triggering after recompositions.
 * @param event The event to be tracked.
 */
@Composable
fun TrackEventSideEffect(
    event: AnalyticsEvent,
) {
    val analyticsReporter = LocalAnalyticsReporter.current
    SideEffect {
        analyticsReporter.report(event)
    }
}
