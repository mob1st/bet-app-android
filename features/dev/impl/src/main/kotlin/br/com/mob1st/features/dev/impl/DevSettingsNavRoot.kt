package br.com.mob1st.features.dev.impl

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import br.com.mob1st.core.design.motion.slide
import br.com.mob1st.features.dev.impl.presentation.gallery.GalleryNavRoot
import br.com.mob1st.features.dev.impl.presentation.gallery.GalleryPage
import br.com.mob1st.features.dev.impl.presentation.menu.DevMenuPage
import br.com.mob1st.features.dev.publicapi.presentation.DevSettingsNavTarget

/**
 * Navigation root for the dev settings feature.
 */
object DevSettingsNavRoot {
    context(NavGraphBuilder)
    fun graph(
        navController: NavController,
        onBack: () -> Unit,
    ) {
        navigation<DevSettingsNavTarget.Route>(
            startDestination = DevSettingsNavTarget.DevMenu,
        ) {
            slide<DevSettingsNavTarget.DevMenu> {
                DevMenuPage(
                    onNext = navController::navigate,
                    onBack = onBack,
                )
            }
            slide<DevSettingsNavTarget.BackendEnvironment> { GalleryPage() }
            slide<DevSettingsNavTarget.FeatureFlags> { GalleryPage() }
            slide<DevSettingsNavTarget.EntryPoints> { GalleryPage() }
            GalleryNavRoot.graph(navController = navController)
        }
    }
}
