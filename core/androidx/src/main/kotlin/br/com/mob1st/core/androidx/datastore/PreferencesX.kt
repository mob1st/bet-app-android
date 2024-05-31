package br.com.mob1st.core.androidx.datastore

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences

/**
 * Set a value in [MutablePreferences] using the [+=] operator if it's not null.
 * Remove the value from [MutablePreferences] using the [-=] operator if it's null.
 */
operator fun <T> MutablePreferences.set(
    key: Preferences.Key<T>,
    value: T?,
) {
    if (value != null) {
        this += key to value
    } else {
        this -= key
    }
}
