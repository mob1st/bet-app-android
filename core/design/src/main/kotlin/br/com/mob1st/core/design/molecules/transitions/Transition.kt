package br.com.mob1st.core.design.molecules.transitions

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * Add a [composable] route to the [NavGraphBuilder] ensuring that the standard [TransitionPattern]s are applied.
 * It uses the default parameters of entering/exiting and pop-entering/pop-exiting transitions, connecting the two
 * routes with the [TransitionPattern]s.
 * @param T the type of the route to be added.
 * @param typeMap the map of [NavType]s to be used by the route. The NavType for the [PatternKey] is added by default.
 * @param deepLinks the list of [NavDeepLink]s to be used by the route.
 * @param content the content to be displayed when the route is navigated to.
 */
inline fun <reified T : TransitionedRoute> NavGraphBuilder.transitioned(
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    val navType = NavType.EnumType(PatternKey::class.java)
    composable<T>(
        typeMap = typeMap + mapOf(typeOf<PatternKey>() to navType),
        deepLinks = deepLinks,
        enterTransition = {
            transition(
                navType = navType,
                entry = targetState,
                isForward = true
            )?.enter()
        },
        exitTransition = {
            transition(
                navType = navType,
                entry = targetState,
                isForward = true
            )?.exit()
        },
        popEnterTransition = {
            transition(
                navType = navType,
                entry = initialState,
                isForward = false
            )?.enter()
        },
        popExitTransition = {
            transition(
                navType = navType,
                entry = initialState,
                isForward = false
            )?.exit()
        },
        content = content,
    )
}

/**
 * Provides the transition pattern to be applied during a navigation event.
 * @param entry the [NavBackStackEntry] to be transitioned.
 * @param isForward whether the navigation event is moving forward or backwards. It can be useful in cases where the
 * transition [BackAndForward] is used.
 * @return the [TransitionPattern] to be applied during the navigation event.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.transition(
    navType: NavType<PatternKey>,
    entry: NavBackStackEntry,
    isForward: Boolean,
): TransitionPattern? {
    val args = entry.arguments ?: bundleOf()
    val patternKey = navType[args, TransitionedRoute::enteringPatternKey.name]
    return when (patternKey) {
        PatternKey.TopLevel -> TopLevel
        PatternKey.BackAndForward -> {
            val towards = if (isForward) {
                AnimatedContentTransitionScope.SlideDirection.Start
            } else {
                AnimatedContentTransitionScope.SlideDirection.End
            }
            BackAndForward(this, towards)
        }

        null -> null
    }
}
