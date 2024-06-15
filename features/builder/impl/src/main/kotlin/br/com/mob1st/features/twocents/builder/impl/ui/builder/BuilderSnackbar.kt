package br.com.mob1st.features.twocents.builder.impl.ui.builder

import br.com.mob1st.core.observability.debug.Debuggable
import br.com.mob1st.core.state.managers.ErrorHandler
import br.com.mob1st.core.state.managers.SnackbarDelegate
import br.com.mob1st.core.state.managers.SnackbarManager
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.features.utils.errors.toCommonError

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

class BuilderSnackbarErrorHandler(
    private val delegate: SnackbarDelegate<BuilderSnackbar> = SnackbarDelegate(),
) : ErrorHandler(), SnackbarManager<BuilderSnackbar> by delegate {
    override fun catch(throwable: Throwable) {
        super.catch(throwable)
        delegate.value = if (throwable is NotEnoughSuggestionsException) {
            BuilderSnackbar.NotEnoughSuggestions(throwable.suggestionsCount)
        } else {
            BuilderSnackbar.Failure(throwable.toCommonError())
        }
    }
}
