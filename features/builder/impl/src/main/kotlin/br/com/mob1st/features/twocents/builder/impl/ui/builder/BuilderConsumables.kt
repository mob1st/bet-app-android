package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategoryInput
import br.com.mob1st.features.utils.errors.CommonError

@Immutable
@optics
data class BuilderConsumables(
    val snackbar: BuilderSnackbar2? = null,
    val navTarget: BuilderNavTarget? = null,
    val dialog: BuilderDialog? = null,
    val bottomSheet: BuilderBottomSheet? = null,
    val isSaving: Boolean = false,
) {
    companion object
}

@Immutable
sealed interface BuilderSnackbar2 {
    @Immutable
    data class NotEnoughSuggestions(
        val remaining: Int,
    ) : BuilderSnackbar2

    @Immutable
    data class SaveError(
        val commonError: CommonError,
    ) : BuilderSnackbar2
}

@Immutable
sealed interface BuilderNavTarget {
    data object Next : BuilderNavTarget
}

@Immutable
sealed interface BuilderDialog {
    @Immutable
    data class CategoryNameDialog(
        val name: String = "",
    ) : BuilderDialog
}

@Immutable
sealed interface BuilderBottomSheet {
    @Immutable
    data class CategorySheet(
        val input: CategoryInput,
        val position: Int?,
    ) : BuilderBottomSheet
}
