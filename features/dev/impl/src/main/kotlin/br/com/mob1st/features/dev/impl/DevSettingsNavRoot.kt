package br.com.mob1st.features.dev.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.mob1st.core.design.atoms.motion.ForwardAndBackward
import br.com.mob1st.core.design.atoms.properties.navigations.navigate
import br.com.mob1st.features.dev.impl.presentation.gallery.Emphasized
import br.com.mob1st.features.dev.impl.presentation.gallery.GalleryNavRoot
import br.com.mob1st.features.dev.impl.presentation.gallery.StandardAccelerate
import br.com.mob1st.features.dev.impl.presentation.gallery.StandardDecelerate
import br.com.mob1st.features.dev.impl.presentation.gallery.durationMillis
import br.com.mob1st.features.dev.impl.presentation.menu.DevMenuPage
import br.com.mob1st.features.dev.publicapi.presentation.DevSettingsNavTarget
import br.com.mob1st.features.utils.navigation.NavRoot
import timber.log.Timber

/**
 * Navigation root for the dev settings feature.
 */
object DevSettingsNavRoot : NavRoot {
    context(NavGraphBuilder) override fun graph(navController: NavController) {
        navigation(
            startDestination = DevSettingsNavTarget.DevMenu.screenName,
            route = DevSettingsNavTarget.ROUTE
        ) {
            val transitionPattern = ForwardAndBackward(
                first = DevSettingsNavTarget.DevMenu,
                second = DevSettingsNavTarget.Gallery
            )
            composable(
                DevSettingsNavTarget.DevMenu.screenName,
                popEnterTransition = {
                    Timber.d("ptest popEnterTransition ${initialState.destination.route}")
                    if (initialState.destination.route == DevSettingsNavTarget.Gallery.screenName) {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(durationMillis, easing = Emphasized)
                        ) +
                            fadeIn(
                                animationSpec = tween(
                                    durationMillis = 300,
                                    delayMillis = 200,
                                    easing = StandardDecelerate
                                )
                            )
                    } else {
                        null
                    }
                },
                exitTransition = {
                    if (targetState.destination.route == DevSettingsNavTarget.Gallery.screenName) {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(durationMillis, easing = Emphasized)
                        ) + fadeOut(
                            animationSpec = tween(
                                durationMillis = 200,
                                easing = StandardAccelerate
                            )
                        )
                    } else {
                        null
                    }
                }

            ) {
                DevMenuPage(navController::navigate)
            }
            GalleryNavRoot.graph(navController)
        }
    }
}
