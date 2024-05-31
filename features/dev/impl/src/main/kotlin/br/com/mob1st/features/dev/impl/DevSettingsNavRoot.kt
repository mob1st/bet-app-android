package br.com.mob1st.features.dev.impl

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import br.com.mob1st.core.design.atoms.motion.ForwardAndBackward
import br.com.mob1st.core.design.atoms.motion.TransitionPattern
import br.com.mob1st.core.design.atoms.motion.composable
import br.com.mob1st.core.design.atoms.properties.navigations.navigate
import br.com.mob1st.features.dev.impl.presentation.gallery.GalleryNavRoot
import br.com.mob1st.features.dev.impl.presentation.gallery.GalleryPage
import br.com.mob1st.features.dev.impl.presentation.menu.DevMenuPage
import br.com.mob1st.features.dev.publicapi.presentation.DevSettingsNavTarget
import br.com.mob1st.features.utils.navigation.NavRoot

/**
 * Navigation root for the dev settings feature.
 */
object DevSettingsNavRoot : NavRoot {
    context(NavGraphBuilder)
    override fun graph(
        navController: NavController,
        patterns: List<TransitionPattern>,
    ) {
        navigation(
            startDestination = DevSettingsNavTarget.DevMenu.screenName,
            route = DevSettingsNavTarget.ROUTE,
        ) {
            composable(
                DevSettingsNavTarget.DevMenu,
                NavGraphTransitions.devMenuTransitions,
            ) { DevMenuPage(navController::navigate) }

            composable(
                DevSettingsNavTarget.BackendEnvironment,
                NavGraphTransitions.backendEnvironmentTransitions,
            ) { GalleryPage() }
            composable(
                DevSettingsNavTarget.FeatureFlags,
                NavGraphTransitions.featureFlagsTransitions,
            ) { GalleryPage() }
            GalleryNavRoot.graph(navController, NavGraphTransitions.galleryTransitions)
            composable(
                DevSettingsNavTarget.EntryPoints,
                NavGraphTransitions.entryPointsTransitions,
            ) { GalleryPage() }
        }
    }

    private object NavGraphTransitions {
        private val menuToBackendEnvironment =
            ForwardAndBackward(
                first = DevSettingsNavTarget.DevMenu,
                second = DevSettingsNavTarget.BackendEnvironment,
            )
        private val menuToFeatureFlags =
            ForwardAndBackward(
                first = DevSettingsNavTarget.DevMenu,
                second = DevSettingsNavTarget.FeatureFlags,
            )
        private val menuToGallery =
            ForwardAndBackward(
                first = DevSettingsNavTarget.DevMenu,
                second = DevSettingsNavTarget.Gallery,
            )
        private val menuToEntryPoints =
            ForwardAndBackward(
                first = DevSettingsNavTarget.DevMenu,
                second = DevSettingsNavTarget.EntryPoints,
            )

        val devMenuTransitions =
            listOf(
                menuToBackendEnvironment,
                menuToFeatureFlags,
                menuToGallery,
                menuToEntryPoints,
            )

        val backendEnvironmentTransitions = listOf(menuToBackendEnvironment)
        val featureFlagsTransitions = listOf(menuToFeatureFlags)
        val galleryTransitions = listOf(menuToGallery)
        val entryPointsTransitions = listOf(menuToEntryPoints)
    }
}
