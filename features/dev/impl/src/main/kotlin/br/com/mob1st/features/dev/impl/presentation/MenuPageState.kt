package br.com.mob1st.features.dev.impl.presentation

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.Text
import br.com.mob1st.core.design.molecules.buttons.ButtonState
import br.com.mob1st.core.design.organisms.lists.ListItemState
import br.com.mob1st.core.design.organisms.snack.SnackState
import br.com.mob1st.features.dev.impl.R
import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment
import br.com.mob1st.features.utils.errors.CommonError

@Immutable
data class MenuPageState(
    private val env: BackendEnvironment? = null,
    private val commonError: CommonError? = null,
    val snack: SnackState? = null,
    val selectedItem: Int? = null,
) {

    val items: List<ListItemState> = env?.let { env ->
        val envResId = when (env) {
            BackendEnvironment.QA -> R.string.dev_menu_list_item_environment_trailing_qa
            BackendEnvironment.STAGING -> R.string.dev_menu_list_item_environment_trailing_staging
            BackendEnvironment.PRODUCTION -> R.string.dev_menu_list_item_environment_trailing_production
        }

        listOf(
            // Environment
            ListItemState(
                headline = Text(R.string.dev_menu_list_ite_environment_headline),
                supporting = Text(R.string.dev_menu_list_item_featureflags_supporting),
                trailing = ListItemState.SupportingText(Text(envResId))
            ),

            // Gallery
            ListItemState(
                headline = Text(R.string.dev_menu_list_item_gallery_headline),
                supporting = Text(R.string.dev_menu_list_item_gallery_supporting)
            ),

            // Feature Flags
            ListItemState(
                headline = Text(R.string.dev_menu_list_item_featureflags_headline),
                supporting = Text(R.string.dev_menu_list_item_featureflags_supporting)
            )
        )
    }.orEmpty()

    val navigationTarget = selectedItem?.let {
        NavigationTarget.Gallery
    }

    val helper = commonError?.let {
        Helper(it is CommonError.NoConnectivity)
    }

    data class Helper(val isNoConnectivity: Boolean) {
        val title: Text = Text(R.string.dev_menu_list_ite_environment_headline)
        val subtitle: Text = Text(R.string.dev_menu_list_item_environment_supporting)
        val button: ButtonState = ButtonState(
            text = Text(R.string.dev_menu_list_item_featureflags_headline)
        )
    }

    sealed interface NavigationTarget {
        data object Gallery : NavigationTarget
    }

    companion object {
        const val GALLERY_POSITION = 1

        val todoSnack: SnackState get() = SnackState(
            supporting = Text("TODO"),
            action = Text("TODO")
        )
    }
}
