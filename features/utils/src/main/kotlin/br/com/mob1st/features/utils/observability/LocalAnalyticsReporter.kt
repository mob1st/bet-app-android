package br.com.mob1st.features.utils.observability

import androidx.compose.runtime.staticCompositionLocalOf
import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.core.observability.events.AnalyticsReporter
import timber.log.Timber

/**
 * Provides the instance for the [AnalyticsReporter] to be used in the composition.
 * The default current value has no effect, but can be used in previews.
 * Don't change the value since it is a static composition local and will affect all the composition tree.
 */
val LocalAnalyticsReporter = staticCompositionLocalOf<AnalyticsReporter> {
    NoOpAnalyticsReporter
}

private object NoOpAnalyticsReporter : AnalyticsReporter {
    override fun report(event: AnalyticsEvent) {
        Timber.d("event: $event")
    }
}
