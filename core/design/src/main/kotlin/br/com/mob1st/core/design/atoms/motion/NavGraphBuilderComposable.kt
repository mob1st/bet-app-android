package br.com.mob1st.core.design.atoms.motion

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.mob1st.core.design.atoms.properties.navigations.NavTarget

/**
 * Create a [composable] with the list of [TransitionPattern]s to animate the navigation.
 *
 * It will use the given [NavTarget.screenName] as route, its [NavTarget.arguments] and [NavTarget.deepLinks].
 * @param current The [NavTarget] to create the [composable].
 * @param list The list of [TransitionPattern]s to animate the navigation.
 * @param content The content of the [composable].
 */
fun NavGraphBuilder.composable(
    current: NavTarget,
    list: List<TransitionPattern>,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = current.screenName,
        arguments = current.arguments,
        deepLinks = current.deepLinks,
        enterTransition = {
            list.firstNotNullEnter {
                enter()
            }
        },
        exitTransition = {
            list.firstNotNullExit {
                exit()
            }
        },
        popEnterTransition = {
            list.firstNotNullEnter {
                popEnter()
            }
        },
        popExitTransition = {
            list.firstNotNullExit {
                popExit()
            }
        },
        content = content
    )
}
