package br.com.mob1st.features.finances.impl.ui.builder.intro

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRoute
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.features.utils.errors.toCommonError

@optics
@Immutable
data class BuilderIntroConsumables(
    val route: BuilderRoute? = null,
    val error: CommonError? = null,
) {
    /**
     * Updates the [error] property with the given [throwable].
     * @param throwable The error.
     * @return The next consumable state.
     */
    fun handleError(throwable: Throwable) = copy(error = throwable.toCommonError())

    companion object
}
