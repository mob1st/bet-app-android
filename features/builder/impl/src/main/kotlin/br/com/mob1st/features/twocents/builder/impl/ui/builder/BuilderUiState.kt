package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Ui state for the category builder
 * @param manuallyAdded The manually added categories.
 * @param suggested The suggested categories.
 */
@Immutable
internal data class BuilderUiState(
    val manuallyAdded: ImmutableList<ListItem<String>> = persistentListOf(),
    val suggested: ImmutableList<ListItem<Int>> = persistentListOf(),
) {
    /**
     * List item state
     */
    @Immutable
    data class ListItem<T>(
        val name: T,
        val amount: InputState = InputState(),
    )
}

@Immutable
internal data class InputState(
    val value: String = "",
    @StringRes val supporting: Int? = null,
)
