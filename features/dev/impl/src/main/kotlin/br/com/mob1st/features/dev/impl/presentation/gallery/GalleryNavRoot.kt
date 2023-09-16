package br.com.mob1st.features.dev.impl.presentation.gallery

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.mob1st.core.design.atoms.properties.navigations.NavTarget
import br.com.mob1st.features.dev.publicapi.presentation.DevSettingsNavTarget
import br.com.mob1st.features.utils.navigation.NavRoot

internal object GalleryNavRoot : NavRoot {
    context(NavGraphBuilder) override fun graph(navController: NavController) {
        navigation(
            route = GalleryNavTarget.ROUTE,
            startDestination = DevSettingsNavTarget.Gallery.screenName
        ) {
            composable(
                route = DevSettingsNavTarget.Gallery.screenName,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )
                }
            ) {
                GalleryPage()
            }
        }
    }
}

internal sealed class GalleryNavTarget : NavTarget() {
    data object Atoms : GalleryNavTarget() {
        override val screenName: String = "atoms"
    }

    data object Molecules : GalleryNavTarget() {
        override val screenName: String = "molecules"
    }

    data object Organisms : GalleryNavTarget() {
        override val screenName: String = "organisms"
    }

    data object Templates : GalleryNavTarget() {
        override val screenName: String = "templates"
    }

    companion object {
        const val ROUTE: String = "br.com.mob1st.features.dev.impl.presentation.gallery"
    }
}
