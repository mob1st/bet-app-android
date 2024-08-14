package br.com.mob1st.core.androidx.navigation

import androidx.navigation.NavHostController
import timber.log.Timber

class ComposeNavigationApi<T : Any>(
    private val navHostController: NavHostController,
) : NavigationApi<T> {
    init {
        Timber.d("ptest ComposeNavigationApi ${hashCode()} -- ${navHostController.hashCode()}")
    }

    override fun navigate(route: T) {
        navHostController.navigate(route, null, null)
    }

    override fun back() {
        navHostController.navigateUp()
    }
}
