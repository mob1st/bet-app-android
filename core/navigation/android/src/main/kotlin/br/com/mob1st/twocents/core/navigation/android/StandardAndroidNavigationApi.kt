package br.com.mob1st.twocents.core.navigation.android

import androidx.navigation.NavHostController
import br.com.mob1st.twocents.core.navigation.NativeNavigationApi

/**
 * Wrapper for the standard onboarding navigation API.
 */
class StandardAndroidNavigationApi(
    private val navHostController: NavHostController,
) : NativeNavigationApi {
    override fun forward(route: Any) {
        navHostController.navigate(route)
    }

    override fun back() {
        navHostController.popBackStack()
    }

    override fun up() {
        navHostController.navigateUp()
    }
}
