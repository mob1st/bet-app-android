package br.com.mob1st.libs.firebase.firestore

import br.com.mob1st.libs.firebase.task.TIMEOUT
import br.com.mob1st.libs.firebase.task.awaitWithTimeout
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Default json serializer for firestore
 */
@OptIn(ExperimentalSerializationApi::class)
val defaultJson = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
    prettyPrint = true
}

/**
 * Run the query and uses json serialization to return the first result as a [T] object.
 *
 * Useful to work with Firestore nested objects
 * @param json the json serializer
 * @param durationInMillis the timeout duration in milliseconds
 */
suspend inline fun <reified T> Query.fetchFirst(
    json: Json = defaultJson,
    durationInMillis: Long = TIMEOUT,
): T {
    return get().awaitWithTimeout(durationInMillis).first().let { doc ->
        val jsonObj = doc.asJson()
        json.decodeFromJsonElement(jsonObj)
    }
}

/**
 * Run the query and uses json serialization to return the result as a [List] of [T] objects.
 *
 * Useful to work with Firestore nested objects
 * @param json the json serializer
 * @param durationInMillis the timeout duration in milliseconds
 */
suspend inline fun <reified T> Query.fetchAll(
    json: Json = defaultJson,
    durationInMillis: Long = TIMEOUT,
): List<T> {
    return get().awaitWithTimeout(durationInMillis).map { doc ->
        val jsonObj = doc.asJson()
        json.decodeFromJsonElement(jsonObj)
    }
}

/**
 * Convert a [DocumentSnapshot] to a [JsonElement] in order to facilitate the serialization of the firestore data.
 *
 * This operation can be expansive and should run out of the main thread.
 * If one of the properties is a [DocumentReference] the id will be used instead of the reference.
 * If one of the properties is a [Timestamp] the date will be converted to ISO 8601 format.
 * If one of the properties is a [List] or a [Map] the operation will be recursive.
 * If one of the properties is primitive (String, Boolean or Number subtype) the value will be used.
 */
fun DocumentSnapshot.asJson(): JsonElement {
    val properties = data.orEmpty() + listOf("id" to id)
    return properties.toJsonElement()
}

private fun Any?.toJsonElement(): JsonElement {
    return when (this) {
        null -> JsonNull
        is Boolean -> JsonPrimitive(this)
        is Number -> JsonPrimitive(this)
        is String -> JsonPrimitive(this)
        is List<*> -> toJsonArray()
        is Map<*, *> -> toJsonObject()
        is Timestamp -> {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val stringDate = sdf.format(toDate())
            JsonPrimitive(stringDate)
        }
        is DocumentReference -> JsonPrimitive(id)
        else -> error("Not supported ${this.javaClass.simpleName}")
    }
}

private fun List<*>.toJsonArray(): JsonArray {
    return JsonArray(map { it.toJsonElement() })
}

private fun Map<*, *>.toJsonObject(): JsonObject {
    val json = entries.associate { it.key!!.toString() to it.value.toJsonElement() }
    return JsonObject(json)
}
