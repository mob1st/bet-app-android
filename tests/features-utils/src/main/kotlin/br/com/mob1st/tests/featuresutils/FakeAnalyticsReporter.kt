package br.com.mob1st.tests.featuresutils

import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.core.observability.events.AnalyticsReporter
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAnalyticsReporter(
    val logState: MutableStateFlow<AnalyticsEvent?> = MutableStateFlow(null),
) : AnalyticsReporter {
    override fun log(event: AnalyticsEvent) {
        logState.value = event
    }
}
