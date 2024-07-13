package br.com.mob1st.core.design.motion.transition

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

interface NavTarget {
    fun pattern(): TransitionPattern
}

inline fun <reified T : Any> NavGraphBuilder.target(
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable<T>(
        deepLinks = deepLinks,
        content = content,
    )
}
