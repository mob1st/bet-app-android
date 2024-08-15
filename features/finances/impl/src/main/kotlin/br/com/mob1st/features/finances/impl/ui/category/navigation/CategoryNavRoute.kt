package br.com.mob1st.features.finances.impl.ui.category.navigation

import br.com.mob1st.core.androidx.navigation.jsonParcelableType
import br.com.mob1st.features.finances.impl.ui.category.detail.CategoryDetailArgs
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

internal sealed interface CategoryNavRoute {
    @Serializable
    data class Detail(val args: CategoryDetailArgs) : CategoryNavRoute

    companion object {
        val navTypes = mapOf(
            typeOf<CategoryDetailArgs>() to jsonParcelableType<CategoryDetailArgs>(),
        )
    }
}
