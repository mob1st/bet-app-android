package br.com.mob1st.core.design.atoms.motion

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.mob1st.core.design.atoms.properties.navigations.NavTarget

/**
 *
 */
fun NavGraphBuilder.composable(
    current: NavTarget,
    list: List<EnterExitSet>,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = current.screenName,
        arguments = current.arguments,
        deepLinks = current.deepLinks,
        enterTransition = {
            list.firstNotNullOfOrNull { pattern ->
                pattern.run {
                    enter()
                }
            }
        },
        exitTransition = {
            list.firstNotNullOfOrNull { pattern ->
                pattern.run {
                    exit()
                }
            }
        },
        popEnterTransition = {
            list.firstNotNullOfOrNull { pattern ->
                pattern.run {
                    popEnter()
                }
            }
        },
        popExitTransition = {
            list.firstNotNullOfOrNull { pattern ->
                pattern.run {
                    popExit()
                }
            }
        },
        content = content
    )
}
