package br.com.mob1st.core.design.motion.transition

import android.os.Build
import android.os.Bundle
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

interface NavTarget {
    val pattern: TransitionPattern

    companion object {
        fun pattern(arguments: Bundle?): TransitionPattern? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable(NavTarget::pattern.name, TransitionPattern::class.java)
            } else {
                @Suppress("DEPRECATION")
                arguments?.getParcelable(NavTarget::pattern.name)
            }
        }
    }
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
