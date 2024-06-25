package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategoryBatch
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategoryInput
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Immutable
@optics
data class BuilderUiState2(
    val categoryType: CategoryType,
    val consumable: BuilderConsumables = BuilderConsumables(),
    val manuallyAddedCategories: PersistentList<BuilderListItem2> = persistentListOf(),
    val suggestedCategories: PersistentList<BuilderListItem2> = persistentListOf(),
    val isSaving: Boolean = false,
) {
    fun toBatch(): CategoryBatch {
        TODO()
    }

    companion object
}

@optics
data class UserInput(
    val consumable: BuilderConsumables = BuilderConsumables(),
    val isSaving: Boolean = false,
    val manuallyAddedCategories: PersistentList<BuilderListItem2> = persistentListOf(),
    val suggestedCategories: PersistentList<BuilderListItem2> = persistentListOf(),
) {
    companion object
}

@Immutable
@optics
data class BuilderListItem2(
    val input: CategoryInput,
) {
    companion object
}
