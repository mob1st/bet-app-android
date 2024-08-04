package br.com.mob1st.core.design.molecules.transitions

import android.os.Bundle
import androidx.navigation.NavType

data object PatternKeyNavType : NavType<PatternKey>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): PatternKey? {
        val ordinal = bundle.getInt(key, -1)
        return if (ordinal != -1) {
            PatternKey.entries[ordinal]
        } else {
            null
        }
    }

    override fun parseValue(value: String): PatternKey {
        return PatternKey.valueOf(value)
    }

    override fun put(bundle: Bundle, key: String, value: PatternKey) {
        bundle.putInt(key, value.ordinal)
    }

    override fun serializeAsValue(value: PatternKey): String {
        return value.name
    }
}
