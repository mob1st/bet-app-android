package br.com.mob1st.libs.firebase.crashlytics

import br.com.mob1st.core.observability.debug.Debuggable
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigFetchThrottledException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigServerException

private const val PACKAGE_INFO = "br.com.mob1st.libs.firebase.crashlytics"
private const val ERROR_SOURCE_KEY = "$PACKAGE_INFO.ERROR_SOURCE"

/**
 * A wrapper for Firebase exceptions used to log some extra information in the crash reporting tool.
 */
class FirebaseDebuggableException(
    cause: FirebaseException,
) : Exception("Firebase library triggered an exception", cause), Debuggable {
    override val logInfo: Map<String, Any?> = when (cause) {
        is FirebaseFirestoreException -> cause.loggedProperties()
        is FirebaseAuthException -> cause.loggedProperties()
        is FirebaseRemoteConfigException -> cause.loggedProperties()
        else -> emptyMap()
    }
}

private fun FirebaseFirestoreException.loggedProperties(): Map<String, Any> = mapOf(
    ERROR_SOURCE_KEY to "firestore",
    "$PACKAGE_INFO.ERROR_CODE" to code.value(),
    "$PACKAGE_INFO.ERROR_NAME" to code.name
)

private fun FirebaseAuthException.loggedProperties(): Map<String, Any> = mapOf(
    ERROR_SOURCE_KEY to "auth",
    "$PACKAGE_INFO.ERROR_CODE" to errorCode
)

private fun FirebaseRemoteConfigException.loggedProperties(): Map<String, Any> {
    val defProperties = mapOf<String, Any>(
        ERROR_SOURCE_KEY to "remoteConfig",
        "$PACKAGE_INFO.CODE" to code
    )
    return defProperties + when (this) {
        is FirebaseRemoteConfigFetchThrottledException -> {
            mapOf("$PACKAGE_INFO.throttleEndTimeMillis" to throttleEndTimeMillis)
        }
        is FirebaseRemoteConfigServerException -> mapOf("$PACKAGE_INFO.httpStatusCode" to httpStatusCode)
        else -> emptyMap()
    }
}
