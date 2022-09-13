package br.com.mob1st.bet.core.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import java.util.Date

@Suppress("UNCHECKED_CAST")
inline fun <reified T> DocumentSnapshot.getNestedObject(fieldName: String): Map<String, T> {
    // firestore triggers an exception when the method get is used to read a nested object
    // so is necessary to get the field using the [data] property and force cast it
    return checkNotNull(data?.get(fieldName))  {
        fieldMessage(fieldName)
    } as Map<String, T>
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

fun fieldMessage(fieldName: String) = "$fieldName must no be null"

/**
 * Triggers the current [Task] with a timeout.
 *
 * Always connect to the task using this function to apply the default project timeout to the
 * requests, because Firebase doesn't provide any configuration for that
 */
suspend fun <T> Task<T>.awaitWithTimeout(durationInMillis: Long = 1_500): T {
    return withTimeout(durationInMillis) {
        await()
    }
}