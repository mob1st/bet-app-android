package br.com.mob1st.features.dev.impl.presentation.menu

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

/**
 * The state of the menu page.
 */
@Immutable
internal sealed class DevMenuUiState {
    /**
     * The state of the menu page when it's loaded.
     */
    @Immutable
    data class Loaded(
        val items: ImmutableList<ListItem>,
    ) : DevMenuUiState()

    /**
     * Represents the state of the menu page when it's empty.
     */
    @Immutable
    data object Empty : DevMenuUiState()

    /**
     * Represents the state of the menu page when it's loading.
     */
    @Immutable
    data class ListItem(
        @StringRes val headline: Int,
        @StringRes val supporting: Int,
        @StringRes val trailing: Int? = null,
    )
}
