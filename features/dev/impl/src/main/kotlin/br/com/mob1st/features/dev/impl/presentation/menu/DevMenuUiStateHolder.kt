package br.com.mob1st.features.dev.impl.presentation.menu

import br.com.mob1st.features.dev.impl.R
import br.com.mob1st.features.dev.impl.domain.DevMenu
import br.com.mob1st.features.dev.impl.domain.DevMenuEntry
import br.com.mob1st.features.dev.impl.presentation.gallery.GalleryNavTarget
import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment
import br.com.mob1st.features.dev.publicapi.presentation.DevSettingsNavTarget
import kotlinx.collections.immutable.toImmutableList

internal class DevMenuUiStateHolder {
    private lateinit var state: DevMenu

    fun asUiState(devMenu: DevMenu): DevMenuUiState {
        this.state = devMenu
        return DevMenuUiState.Loaded(
            items = state.entries.map { entry ->
                entry.toListItem()
            }.toImmutableList(),
        )
    }

    fun isImplemented(position: Int): Boolean {
        return state.entries[position].isImplemented
    }

    fun getNavTarget(position: Int) =
        when (state.entries[position]) {
            DevMenuEntry.Environment -> DevSettingsNavTarget.BackendEnvironment
            DevMenuEntry.Gallery -> GalleryNavTarget.Root
            DevMenuEntry.FeatureFlags -> DevSettingsNavTarget.FeatureFlags
            DevMenuEntry.EntryPoint -> DevSettingsNavTarget.EntryPoints
        }

    private fun DevMenuEntry.toListItem(): DevMenuUiState.ListItem {
        return when (this) {
            DevMenuEntry.Environment -> {
                val envResId = when (state.backendEnvironment) {
                    BackendEnvironment.QA -> R.string.dev_menu_list_item_environment_trailing_qa
                    BackendEnvironment.STAGING -> R.string.dev_menu_list_item_environment_trailing_staging
                    BackendEnvironment.PRODUCTION -> R.string.dev_menu_list_item_environment_trailing_production
                }
                DevMenuUiState.ListItem(
                    headline = R.string.dev_menu_list_item_environment_headline,
                    supporting = R.string.dev_menu_list_item_featureflags_supporting,
                    trailing = envResId,
                )
            }

            DevMenuEntry.FeatureFlags -> DevMenuUiState.ListItem(
                headline = R.string.dev_menu_list_item_featureflags_headline,
                supporting = R.string.dev_menu_list_item_featureflags_supporting,
            )

            DevMenuEntry.Gallery -> DevMenuUiState.ListItem(
                headline = R.string.dev_menu_list_item_gallery_headline,
                supporting = R.string.dev_menu_list_item_gallery_supporting,
            )

            DevMenuEntry.EntryPoint -> DevMenuUiState.ListItem(
                headline = R.string.dev_menu_list_item_entrypoints_headline,
                supporting = R.string.dev_menu_list_item_entrypoints_supporting,
            )
        }
    }
}
