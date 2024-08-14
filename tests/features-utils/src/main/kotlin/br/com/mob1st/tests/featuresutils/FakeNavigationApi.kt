package br.com.mob1st.tests.featuresutils

import br.com.mob1st.twocents.core.navigation.NativeNavigationApi

/**
 * Fake implementation for the navigation API.
 * it just add and remove routes from the [routes] list.
 */
class FakeNavigationApi : NativeNavigationApi {
    /**
     * The list of routes that were navigated.
     */
    val routes = mutableListOf<Any>()

    override fun back() {
        routes.removeAt(routes.size - 1)
    }

    override fun forward(route: Any) {
        routes.add(route)
    }

    override fun up() {
        routes.removeAt(routes.size - 1)
    }
}
