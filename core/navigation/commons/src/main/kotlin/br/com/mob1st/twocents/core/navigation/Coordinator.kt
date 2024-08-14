package br.com.mob1st.twocents.core.navigation

/**
 * A coordinator is responsible for navigating between screens.
 * It's an abstraction on top of the native navigation framework, handling the communication between the different part
 * of the app, like different features or even screens.
 * It's common to have a coordinator for each feature, but it's also possible to have a coordinator for a group of
 * features can be the case.
 * @param T The type of the route to navigate to.
 */
abstract class Coordinator<T : Any>(
    protected val api: NativeNavigationApi,
) {
    /**
     * Navigate to the given route.
     */
    protected open fun navigate(route: T) {
        api.forward(route)
    }

    /**
     * Navigate back to the previous screen.
     */
    open fun back() {
        api.back()
    }

    /**
     * Navigate up to the parent screen.
     */
    open fun up() {
        api.up()
    }
}
