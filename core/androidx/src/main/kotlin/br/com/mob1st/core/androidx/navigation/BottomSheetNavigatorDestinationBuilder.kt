package br.com.mob1st.core.androidx.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDestinationBuilder
import androidx.navigation.NavDestinationDsl
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.get
import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * Add the [content] [Composable] as bottom sheet content to the [NavGraphBuilder]
 * @param route route for the destination
 * @param navigator the navigator that will handle the destination
 * @param typeMap the type map for the destination
 * @param content the sheet content at the given destination
 */
@NavDestinationDsl
class BottomSheetNavigatorDestinationBuilder : NavDestinationBuilder<BottomSheetNavigator.Destination> {
    private val bottomSheetNavigator: BottomSheetNavigator
    private val content: @Composable (NavBackStackEntry) -> Unit

    constructor(
        navigator: BottomSheetNavigator,
        route: String,
        content: @Composable (NavBackStackEntry) -> Unit,
    ) : super(navigator, route) {
        this.bottomSheetNavigator = navigator
        this.content = content
    }

    constructor(
        navigator: BottomSheetNavigator,
        route: KClass<*>,
        typeMap: Map<KType, @JvmSuppressWildcards NavType<*>>,
        content: @Composable (NavBackStackEntry) -> Unit,
    ) : super(navigator, route, typeMap) {
        this.bottomSheetNavigator = navigator
        this.content = content
    }

    override fun instantiateDestination(): BottomSheetNavigator.Destination {
        return BottomSheetNavigator.Destination(bottomSheetNavigator, content)
    }
}

/**
 * Add a destination that will be displayed as a bottom sheet using the type-safe builder.
 * @param T the type of the destination
 * @param typeMap the type map for the destination
 * @param deepLinks list of deep links to associate with the destinations
 */
inline fun <reified T : Any> NavGraphBuilder.bottomSheet(
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable (backstackEntry: NavBackStackEntry) -> Unit,
) {
    destination(
        BottomSheetNavigatorDestinationBuilder(
            provider[BottomSheetNavigator::class],
            T::class,
            typeMap,
            content,
        ).apply {
            deepLinks.forEach { deepLink -> deepLink(deepLink) }
        },
    )
}
