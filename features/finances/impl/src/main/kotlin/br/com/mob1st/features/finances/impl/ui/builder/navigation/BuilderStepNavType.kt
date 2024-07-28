package br.com.mob1st.features.finances.impl.ui.builder.navigation

import android.os.Bundle
import androidx.navigation.NavType

/**
 * Navigation type for [BuilderRoute.Step.Type].
 */
internal object BuilderStepNavType : NavType<BuilderRoute.Step.Type>(false) {
    override fun get(bundle: Bundle, key: String): BuilderRoute.Step.Type? {
        val ordinal = bundle.getInt(key)
        return BuilderRoute.Step.Type.entries.getOrNull(ordinal)
    }

    override fun parseValue(value: String): BuilderRoute.Step.Type {
        return BuilderRoute.Step.Type.valueOf(value)
    }

    override fun put(bundle: Bundle, key: String, value: BuilderRoute.Step.Type) {
        bundle.putInt(key, value.ordinal)
    }

    override fun serializeAsValue(value: BuilderRoute.Step.Type): String {
        return value.name
    }
}
