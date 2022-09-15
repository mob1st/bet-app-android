package br.com.mob1st.bet.core.firebase

import br.com.mob1st.bet.core.utils.extensions.toFormat
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.util.Date

@Suppress("UNCHECKED_CAST")
inline fun <reified T> DocumentSnapshot.getNestedObject(fieldName: String): Map<String, T> {
    // firestore triggers an exception when the method get is used to read a nested object
    // so is necessary to get the field using the [data] property and force cast it
    return checkNotNull(data?.get(fieldName))  {
        fieldMessage(fieldName)
    } as Map<String, T>
}

/**
 * Get a nested field in a map structure
 *
 * Useful to work with Firestore nested objects
 */
@Suppress("UNCHECKED_CAST")
fun Map<String, *>.getNestedMap(fieldName: String): Map<String, Any> {
    return checkNotNull(get(fieldName)) {
        fieldMessage(fieldName)
    } as Map<String, Any>
}

/**
 * Get a list field in a map structure
 *
 * Useful to work with Firestore nested arrays
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> Map<String, *>.getNestedList(fieldName: String): List<T> {
    return checkNotNull(get(fieldName)) {
        fieldMessage(fieldName)
    } as List<T>
}

fun DocumentSnapshot.getDateNotNull(fieldName: String): Date {
    return checkNotNull(getDate(fieldName)) {
        fieldMessage(fieldName)
    }
}

fun DocumentSnapshot.getLongNotNull(fieldName: String): Long {
    return checkNotNull(getLong(fieldName)) {
        fieldMessage(fieldName)
    }
}

fun DocumentSnapshot.getStringNotNull(fieldName: String): String {
    return checkNotNull(getString(fieldName)) {
        fieldMessage(fieldName)
    }
}

fun DocumentSnapshot.getBooleanNotNull(fieldName: String): Boolean {
    return checkNotNull(getBoolean(fieldName)) {
        fieldMessage(fieldName)
    }
}

fun DocumentSnapshot.asJson(): JsonElement {
    return data.toJsonElement()
}

fun Map<*, *>.toJsonObject(): JsonElement {
    val json = entries.associate { it.key!!.toString() to it.value.toJsonElement() }
    return JsonObject(json)
}

private fun List<*>.toJsonArray(): JsonElement {
    return JsonArray(map { it.toJsonElement() })
}

private fun Any?.toJsonElement(): JsonElement {
    return when (this) {
        null -> JsonNull
        is Boolean -> JsonPrimitive(this)
        is Number -> JsonPrimitive(this)
        is String -> JsonPrimitive(this)
        is Date -> JsonPrimitive(toFormat())
        is List<*> -> toJsonArray()
        is Map<*, *> -> toJsonObject()
        is DocumentReference -> JsonPrimitive(id)
        else -> throw IllegalStateException("invalid ")
    }
}

fun fieldMessage(fieldName: String) = "$fieldName must no be null"

/**
 * Triggers the current [Task] with a timeout.
 *
 * Always connect to the task using this function to apply the default project timeout to the
 * requests, because Firebase doesn't provide any configuration for that
 */
suspend fun <T> Task<T>.awaitWithTimeout(durationInMillis: Long = 2_500): T {
    return withTimeout(durationInMillis) {
        await()
    }
}