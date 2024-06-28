package br.com.mob1st.features.twocents.builder.impl.ui.builder

import br.com.mob1st.core.observability.debug.Debuggable
import br.com.mob1st.features.utils.errors.CommonError

sealed interface BuilderSnackbar {
    data class NotEnoughSuggestions(
        val suggestionsCount: Int,
    ) : BuilderSnackbar

    data class Failure(val error: CommonError) : BuilderSnackbar
}

class NotEnoughSuggestionsException(
    val suggestionsCount: Int,
) : Exception(), Debuggable {
    override val logInfo: Map<String, Any?> = mapOf(
        "suggestionsCount" to suggestionsCount,
    )
}
