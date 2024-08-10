package br.com.mob1st.core.androidx.parcelables

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

/**
 * Gets a [Parcelable] from the [Bundle] using the [key].
 * It casts the result to the type [T], using different [Parcelable] methods depending on the Android version.
 * @param key The key to get the [Parcelable] from the [Bundle].
 * @param T The type of the [Parcelable] to be returned.
 * @return The [Parcelable] casted to the type [T] or null if the [Parcelable] is not found or if the cast fails.
 */
inline fun <reified T : Parcelable> Bundle.getParcelableAs(key: String): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelable(key) as? T
    }
