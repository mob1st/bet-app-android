package br.com.mob1st.features.finances.impl.infra.data.system

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.StringRes

/**
 * Provides an integer identifier for a given string.
 */
interface StringIdGetter {
    /**
     * Returns the integer identifier for the given string.
     * @param identifier The string identifier.
     * @return The integer identifier or null if not found.
     */
    @StringRes
    operator fun get(identifier: String): Int?
}

/**
 * A wrapper for the Android system API to get the integer identifier for a given string.
 * @property context The Android context that will be used to get the string identifier.
 */
internal class AndroidStringIdGetter(
    private val context: Context,
) : StringIdGetter {
    private val cache = mutableMapOf<String, Int>()

    @SuppressLint("DiscouragedApi")
    override operator fun get(identifier: String): Int? {
        val id = cache.getOrPut(identifier) {
            context
                .resources
                .getIdentifier(identifier, "string", context.packageName)
        }
        return if (id > 0) {
            id
        } else {
            null
        }
    }
}
