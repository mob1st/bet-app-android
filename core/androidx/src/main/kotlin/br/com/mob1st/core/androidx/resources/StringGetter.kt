package br.com.mob1st.core.androidx.resources

import android.content.Context
import androidx.annotation.StringRes

/**
 * Provides a string for a given integer identifier.
 */
interface StringGetter {
    /**
     * Returns the string for the given integer identifier.
     * @param identifier The integer identifier.
     * @return The localized string based on the identifier.
     */
    operator fun get(
        @StringRes identifier: Int,
    ): String
}

class AndroidStringGetter(
    private val context: Context,
) : StringGetter {
    override operator fun get(
        @StringRes identifier: Int,
    ): String {
        return context.getString(identifier)
    }
}
