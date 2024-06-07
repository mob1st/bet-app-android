package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.TextState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Ui state for the category builder
 * @param manuallyAdded The manually added categories.
 * @param suggested The suggested categories.
 */
@Immutable
internal data class BuilderUiState(
    val manuallyAdded: ImmutableList<ListItem> = persistentListOf(),
    val suggested: ImmutableList<ListItem> = persistentListOf(),
) {
    /**
     * List item state
     */
    @Immutable
    data class ListItem(
        val name: TextState,
        val value: String = "",
        val isFocused: Boolean = false,
    )
}
