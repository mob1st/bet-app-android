package br.com.mob1st.features.finances.impl.ui.builder

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.utils.errors.CommonError

@optics
data class CategoryBuilderConsumables(
    val dialog: CategoryBuilderDialog? = null,
    val navTarget: CategoryBuilderNavTarget? = null,
    val snackbar: CategoryBuilderSnackbar? = null,
) {
    companion object
}

@Immutable
sealed interface CategoryBuilderDialog {
    @Immutable
    @optics
    data class EnterName(
        val name: String,
        val isSubmitEnabled: Boolean,
    ) : CategoryBuilderDialog {
        companion object
    }
}

@Immutable
sealed interface CategoryBuilderNavTarget {
    @Immutable
    data class NextStep(val step: BuilderNextAction.Step)

    @Immutable
    data object BuilderCompletion
}

@Immutable
sealed interface CategoryBuilderSnackbar {
    data class NotEnoughInputs(val remaining: Int) : CategoryBuilderSnackbar

    data class Failure(val commonError: CommonError) : CategoryBuilderSnackbar
}
