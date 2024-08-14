package br.com.mob1st.tests.featuresutils

import br.com.mob1st.twocents.core.navigation.NativeNavigationApi

class FakeNavigationApi : NativeNavigationApi {
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
