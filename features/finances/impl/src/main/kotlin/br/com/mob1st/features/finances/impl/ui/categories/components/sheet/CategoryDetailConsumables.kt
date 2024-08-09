package br.com.mob1st.features.finances.impl.ui.categories.components.sheet

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.utils.errors.CommonErrorSnackbarState

@Immutable
@optics
data class CategoryDetailConsumables(
    val dialog: Dialog? = null,
    val commonErrorSnackbarState: CommonErrorSnackbarState? = null,
) {
    sealed interface Dialog

    companion object
}

@Immutable
data class CategoryNameDialog(
    val name: String,
) : CategoryDetailConsumables.Dialog

@Immutable
data class CategoryCalendarDialog(
    val recurrences: Recurrences,
) : CategoryDetailConsumables.Dialog
