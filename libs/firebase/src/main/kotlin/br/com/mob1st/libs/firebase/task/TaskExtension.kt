package br.com.mob1st.libs.firebase.task

import br.com.mob1st.libs.firebase.crashlytics.FirebaseDebuggableException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

/**
 * Default timeout for tasks.
 */
const val TIMEOUT = 3_000L

/**
 * Await for a task to finish with a timeout.
 *
 * It wraps the [FirebaseException] in a [FirebaseDebuggableException] so it can be logged in the crashlytics.
 *
 * @param durationInMillis the timeout duration in milliseconds. Default is 3 seconds.
 * @throws FirebaseDebuggableException if the task throws a [FirebaseException]
 */
suspend fun <T> Task<T>.awaitWithTimeout(durationInMillis: Long = TIMEOUT): T {
    return withTimeout(durationInMillis) {
        try {
            await()
        } catch (e: FirebaseException) {
            throw FirebaseDebuggableException(e)
        }
    }
}
