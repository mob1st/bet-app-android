package br.com.mob1st.features.dev.impl.presentation.gallery

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.mob1st.core.design.atoms.properties.navigations.NavTarget
import br.com.mob1st.features.dev.publicapi.presentation.DevSettingsNavTarget
import br.com.mob1st.features.utils.navigation.NavRoot

val Emphasized = CubicBezierEasing(0.2f, 0f, 0f, 1f)
val EmphasizedDecelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
val EmphasizedAccelerate = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)

val Standard = CubicBezierEasing(0.2f, 0f, 0f, 1f)

val StandardAccelerate = CubicBezierEasing(0.3f, 0f, 1f, 1f)

val StandardDecelerate = CubicBezierEasing(0f, 0f, 0f, 1f)

const val durationMillis = 500

internal object GalleryNavRoot : NavRoot {
    context(NavGraphBuilder) override fun graph(navController: NavController) {
        navigation(
            route = GalleryNavTarget.ROUTE,
            startDestination = DevSettingsNavTarget.Gallery.screenName
        ) {
            composable(
                route = DevSettingsNavTarget.Gallery.screenName,
                enterTransition = {
                    if (initialState.destination.route == DevSettingsNavTarget.DevMenu.screenName) {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(
                                durationMillis = durationMillis,
                                easing = Emphasized
                            )
                        ) + fadeIn(
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
                popExitTransition = {
                    if (targetState.destination.route == DevSettingsNavTarget.DevMenu.screenName) {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(durationMillis, easing = Emphasized)
                        ) + fadeOut(animationSpec = tween(durationMillis = 200, easing = StandardAccelerate))
                    } else {
                        null
                    }
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
