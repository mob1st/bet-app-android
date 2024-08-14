package br.com.mob1st.core.androidx.navigation

interface NavigationApi<T> {
    fun navigate(route: T)

    fun back()
}
