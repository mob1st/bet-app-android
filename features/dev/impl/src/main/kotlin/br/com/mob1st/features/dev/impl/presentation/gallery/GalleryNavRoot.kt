package br.com.mob1st.features.dev.impl.presentation.gallery

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import br.com.mob1st.core.design.motion.slide
import br.com.mob1st.features.dev.publicapi.presentation.DevSettingsNavTarget
import br.com.mob1st.features.utils.navigation.NavRoot
import kotlinx.serialization.Serializable

internal object GalleryNavRoot : NavRoot {
    context(NavGraphBuilder)
    override fun graph(navController: NavController) {
        navigation<GalleryNavTarget.Route>(
            startDestination = GalleryNavTarget.Root,
        ) {
            slide<GalleryNavTarget.Root> {
                GalleryPage()
            }
        }
    }
}

internal sealed class GalleryNavTarget : DevSettingsNavTarget.Nested {
    @Serializable
    data object Root : GalleryNavTarget()

    @Serializable
    data object Atoms : GalleryNavTarget()

    @Serializable
    data object Molecules : GalleryNavTarget()

    @Serializable
    data object Organisms : GalleryNavTarget()

    @Serializable
    data object Templates : GalleryNavTarget()

    @Serializable
    object Route
}
