package br.com.mob1st.core.androidx.resources

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
    fun getId(identifier: String): Int?

    /**
     * Returns the actual localized string for the given identifier.
     * It uses the return of [getId] to get the string.
     * If getId returns null, this method will also return null.
     * @param identifier The string identifier.
     * @return The localized string or null if not found.
     */
    fun getString(identifier: String): String?
}

/**
 * A wrapper for the Android system API to get the integer identifier for a given string.
 * @property context The Android context that will be used to get the string identifier.
 */
class AndroidStringIdGetter(
    private val context: Context,
) : StringIdGetter {
    private val cache = mutableMapOf<String, Int>()

    @SuppressLint("DiscouragedApi")
    override fun getId(identifier: String): Int? {
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

    override fun getString(identifier: String): String? {
        return getId(identifier)?.let {
            context.getString(it)
        }
    }
}
