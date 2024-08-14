package br.com.mob1st.features.finances.impl.ui.builder.navigation

import br.com.mob1st.core.androidx.navigation.NavigationApi

/**
 * The router for the budget builder feature flow.
 * It intermediates the navigation events sent from ViewModels and maps it to routes that can be navigated by the UI.
 * Also, it has capabilities to extract the routes parameters to domain specific data, that can be used as arguments by
 * screens.
 */
internal class BuilderRouter(
    private val navigationApi: NavigationApi<BuilderNavRoute>,
)
