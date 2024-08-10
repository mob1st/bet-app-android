package br.com.mob1st.core.androidx.navigation

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import br.com.mob1st.core.androidx.parcelables.getParcelableAs
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Nav type for type-safe navigation arguments.
 * Ensure that the class of type [T] is annotated with [kotlinx.parcelize.Parcelize] AND
 * [kotlinx.serialization.Serializable].
 * Then use this [NavType] to navigate between destinations with type-safe arguments.
 * It's important to mention that this [NavType] uses reflection to get the type of the class, which means that it's
 * important to avoid to use large objects structures during navigation to avoid performance issues.
 * @param T the type of the class that will be used as argument.
 * @param isNullableAllowed if the argument is nullable.
 * @param json the [Json] instance to encode and decode the argument.
 * @return a [NavType] for type-safe navigation arguments.
 */
inline fun <reified T : Parcelable> jsonParcelableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed) {
    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getParcelableAs(key)
    }

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String {
        val jsonEnconded = json.encodeToString<T>(value)
        return Uri.encode(jsonEnconded)
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putParcelable(key, value)
    }
}
