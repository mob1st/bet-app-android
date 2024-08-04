package br.com.mob1st.features.finances.impl.ui.builder.navigation

import android.os.Bundle
import androidx.navigation.NavType

/**
 * Navigation type for [BuilderNavEvent.Step].
 */
internal object BuilderStepNavType : NavType<BuilderNavRoute.Step.Id>(false) {
    override fun get(bundle: Bundle, key: String): BuilderNavRoute.Step.Id {
        val ordinal = bundle.getInt(key)
        return BuilderNavRoute.Step.Id.entries[ordinal]
    }

    override fun parseValue(value: String): BuilderNavRoute.Step.Id {
        return BuilderNavRoute.Step.Id.valueOf(value)
    }

    override fun put(bundle: Bundle, key: String, value: BuilderNavRoute.Step.Id) {
        bundle.putInt(key, value.ordinal)
    }

    override fun serializeAsValue(value: BuilderNavRoute.Step.Id): String {
        return value.name
    }
}
