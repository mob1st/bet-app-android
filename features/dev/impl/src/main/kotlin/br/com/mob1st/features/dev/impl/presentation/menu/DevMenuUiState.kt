package br.com.mob1st.features.dev.impl.presentation.menu

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.Text
import br.com.mob1st.core.design.organisms.lists.ListItemState
import br.com.mob1st.core.design.organisms.snack.SnackbarState
import br.com.mob1st.features.dev.impl.R
import br.com.mob1st.features.dev.impl.domain.DevMenu
import br.com.mob1st.features.dev.impl.domain.DevMenuEntry
import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment

/**
 * The state of the menu page.
 */
@Immutable
internal sealed class DevMenuUiState {
    /**
     * The state of the menu page when it's loaded.
     */
    data class Loaded(
        val menu: DevMenu,
    ) : DevMenuUiState() {
        val items: List<ListItemState> =
            menu.entries.map { entry ->
                toListItem(menu.backendEnvironment, entry)
            }
    }

    /**
     * Represents the state of the menu page when it's empty.
     */
    data object Empty : DevMenuUiState()

    companion object {
        /**
         * The snack displayed when the selected feature is not implemented yet.
         */
        fun TodoSnack(): SnackbarState =
            SnackbarState(
                supporting = Text(R.string.dev_menu_snack_todo_supporting),
            )
    }
}

private fun toListItem(
    backendEnvironment: BackendEnvironment,
    entry: DevMenuEntry,
): ListItemState {
    return when (entry) {
        DevMenuEntry.Environment -> {
            val envResId =
                when (backendEnvironment) {
                    BackendEnvironment.QA -> R.string.dev_menu_list_item_environment_trailing_qa
                    BackendEnvironment.STAGING -> R.string.dev_menu_list_item_environment_trailing_staging
                    BackendEnvironment.PRODUCTION -> R.string.dev_menu_list_item_environment_trailing_production
                }
            ListItemState(
                headline = Text(R.string.dev_menu_list_ite_environment_headline),
                supporting = Text(R.string.dev_menu_list_item_featureflags_supporting),
                trailing = ListItemState.Text(Text(envResId)),
            )
        }
        DevMenuEntry.FeatureFlags ->
            ListItemState(
                headline = Text(R.string.dev_menu_list_item_featureflags_headline),
                supporting = Text(R.string.dev_menu_list_item_featureflags_supporting),
            )
        DevMenuEntry.Gallery ->
            ListItemState(
                headline = Text(R.string.dev_menu_list_item_gallery_headline),
                supporting = Text(R.string.dev_menu_list_item_gallery_supporting),
            )
        DevMenuEntry.EntryPoint ->
            ListItemState(
                headline = Text(R.string.dev_menu_list_item_entrypoints_headline),
                supporting = Text(R.string.dev_menu_list_item_entrypoints_supporting),
            )
    }
}
