package br.com.mob1st.twocents.core.navigation

/**
 * Wrapper on top of the native navigation framework.
 * It handles the specific tool used to navigate between screens.
 */
interface NativeNavigationApi {
    /**
     * Back navigation, going to the previous screen.
     */
    fun back()

    /**
     * Forward navigation, going to the next screen.
     */
    fun forward(route: Any)

    /**
     * Up navigation, going to the parent screen.
     */
    fun up()
}
