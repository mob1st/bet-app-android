package br.com.mob1st.twocents.core.navigation.android

import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import br.com.mob1st.twocents.core.navigation.NativeNavigationApi

/**
 * Wrapper for the standard onboarding navigation API.
 */
class StandardAndroidNavigationApi(
    val navHostController: NavHostController,
) : NativeNavigationApi {
    override fun forward(route: Any) {
        navHostController.navigate(route)
    }

    override fun back() {
        navHostController.popBackStack(route = false, inclusive = false)
    }

    override fun up() {
        navHostController.navigateUp()
    }

    inline fun <reified T : Any> findInBackStacks(): T {
        val entry = navHostController.getBackStackEntry<T>()
        return entry.toRoute<T>()
    }
}
