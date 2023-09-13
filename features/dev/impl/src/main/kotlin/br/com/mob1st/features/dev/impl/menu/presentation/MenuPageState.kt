package br.com.mob1st.features.dev.impl.menu.presentation

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.Text
import br.com.mob1st.core.design.molecules.buttons.ButtonState
import br.com.mob1st.core.design.organisms.lists.ListItemState
import br.com.mob1st.core.design.organisms.snack.SnackState
import br.com.mob1st.features.dev.impl.R
import br.com.mob1st.features.dev.impl.menu.domain.DevMenu
import br.com.mob1st.features.dev.impl.menu.domain.DevMenuEntry
import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment
import br.com.mob1st.features.utils.errors.CommonError

/**
 * Represents the state of the menu page.
 */
@Immutable
sealed class MenuPageState {

    /**
     * Represents the state of the menu page when it's loaded.
     */
    data class Loaded(
        val menu: DevMenu,
        val snack: SnackState? = null,
        val selectedItem: Int? = null,
    ) : MenuPageState() {
        val items: List<ListItemState> = menu.entries.map(::toListItem)
        val navigationTarget = selectedItem?.let {
            when (menu.entries[selectedItem]) {
                DevMenuEntry.Gallery -> NavigationTarget.Gallery
                else -> null
            }
        }
    }

    /**
     * Represents the state of the menu page when it's failed to load.
     */
    data class Failed(val commonError: CommonError?) : MenuPageState() {
        val helper = Helper(commonError == CommonError.NoConnectivity)
    }

    /**
     * Represents the state of the menu page when it's empty.
     */
    data object Empty : MenuPageState()

    companion object {

        /**
         * Transforms the result of the use case into a [MenuPageState].
         */
        fun transform(result: Result<DevMenu>, snack: SnackState? = null, selectedItem: Int? = null) = result.map {
            Loaded(it, snack, selectedItem)
        }.getOrElse {
            Failed(CommonError.from(it))
        }

        val todoSnack: SnackState get() = SnackState(
            supporting = Text("TODO"),
            action = Text("TODO")
        )
    }
}

/**
 * Represents the navigation target of the menu page.
 */
sealed interface NavigationTarget {
    data object Gallery : NavigationTarget
}

private fun toListItem(entry: DevMenuEntry): ListItemState {
    return when (entry) {
        is DevMenuEntry.Environment -> {
            val envResId = when (entry.type) {
                BackendEnvironment.QA -> R.string.dev_menu_list_item_environment_trailing_qa
                BackendEnvironment.STAGING -> R.string.dev_menu_list_item_environment_trailing_staging
                BackendEnvironment.PRODUCTION -> R.string.dev_menu_list_item_environment_trailing_production
            }
            ListItemState(
                headline = Text(R.string.dev_menu_list_ite_environment_headline),
                supporting = Text(R.string.dev_menu_list_item_featureflags_supporting),
                trailing = ListItemState.SupportingText(Text(envResId))
            )
        }
        DevMenuEntry.FeatureFlags -> ListItemState(
            headline = Text(R.string.dev_menu_list_item_featureflags_headline),
            supporting = Text(R.string.dev_menu_list_item_featureflags_supporting)
        )
        DevMenuEntry.Gallery -> ListItemState(
            headline = Text(R.string.dev_menu_list_item_gallery_headline),
            supporting = Text(R.string.dev_menu_list_item_gallery_supporting)
        )
        DevMenuEntry.EntryPoint -> ListItemState(
            headline = Text(R.string.dev_menu_list_item_featureflags_headline),
            supporting = Text(R.string.dev_menu_list_item_gallery_supporting)
        )
    }
}

data class Helper(val isNoConnectivity: Boolean) {
    val title: Text = Text(R.string.dev_menu_list_ite_environment_headline)
    val subtitle: Text = Text(R.string.dev_menu_list_item_environment_supporting)
    val button: ButtonState = ButtonState(
        text = Text(R.string.dev_menu_list_item_featureflags_headline)
    )
}
