package br.com.mob1st.features.finances.impl.ui.category.navigation

import br.com.mob1st.core.design.molecules.transitions.NavRoute
import br.com.mob1st.core.design.molecules.transitions.PatternKey
import br.com.mob1st.features.finances.impl.ui.category.components.sheet.Args
import kotlinx.serialization.Serializable

sealed interface CategoryNavRoute : NavRoute {
    @Serializable
    data class Detail(
        override val enteringPatternKey: PatternKey? = null,
        val args: Args,
    ) : CategoryNavRoute
}
