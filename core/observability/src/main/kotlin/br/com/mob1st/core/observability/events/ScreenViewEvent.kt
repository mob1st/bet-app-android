package br.com.mob1st.core.observability.events

data class ScreenViewEvent(
    val screenName: String,
    val params: Map<String, Any> = emptyMap(),
) : AnalyticsEvent {

    override val name: String = "screen_view"
    override val logInfo: Map<String, Any?> = mapOf(
        "screen_name" to screenName
    ) + params
}
