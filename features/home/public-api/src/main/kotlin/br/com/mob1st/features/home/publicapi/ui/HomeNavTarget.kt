package br.com.mob1st.features.home.publicapi.ui

import br.com.mob1st.core.navigation.NavTarget

/**
 * Home navigation targets.
 */
sealed class HomeNavTarget : NavTarget() {

    /**
     * Home screen.
     */
    data object Home : HomeNavTarget() {
        override val screenName: String = "home"
    }

    companion object {
        const val ROUTE = "br.com.mob1st.features.home"
    }
}
