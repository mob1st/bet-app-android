package br.com.mob1st.features.finances.impl.infra.data.system

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.StringRes

/**
 * Provides an integer identifier for a given string.
 */
interface StringIdProvider {
    /**
     * Returns the integer identifier for the given string.
     * @param identifier The string identifier.
     * @return The integer identifier or null if not found.
     */
    @StringRes
    operator fun get(identifier: String): Int?
}

class AndroidStringIdProvider(
    private val context: Context,
) : StringIdProvider {
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
