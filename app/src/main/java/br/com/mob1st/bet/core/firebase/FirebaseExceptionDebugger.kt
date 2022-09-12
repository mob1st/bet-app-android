package br.com.mob1st.bet.core.firebase

import br.com.mob1st.bet.core.logs.ThrowableDebugger
import br.com.mob1st.bet.core.logs.getPropertiesTree
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigClientException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigFetchThrottledException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigServerException

object FirebaseExceptionDebugger : ThrowableDebugger<FirebaseException> {
    override fun propertiesFrom(throwable: FirebaseException): Map<String, Any> {
        return when(throwable) {
            is FirebaseFirestoreException -> throwable.loggedProperties()
            is FirebaseAuthException -> throwable.loggedProperties()
            is FirebaseRemoteConfigFetchThrottledException -> throwable.loggedProperties()
            is FirebaseRemoteConfigServerException -> throwable.loggedProperties()
            else -> emptyMap()
        }
    }
}

fun FirebaseFirestoreException.loggedProperties(): Map<String, Any> {
    return mapOf(
        "errorCode" to code.value(),
        "errorName" to code.name,
        "errorSource" to "firestore"
    )
}

private fun FirebaseAuthException.loggedProperties(): Map<String, Any> {
    return mapOf(
        "errorCode" to errorCode,
        "errorSource" to "authentication"
    )
}

private fun FirebaseRemoteConfigFetchThrottledException.loggedProperties(): Map<String, Any> {
    return mapOf(
        "throttleEndTimeMillis" to throttleEndTimeMillis,
        "errorSource" to "remoteConfig"
    )
}

private fun FirebaseRemoteConfigServerException.loggedProperties(): Map<String, Any> {
    return mapOf(
        "httpStatusCode" to httpStatusCode,
        "errorSource" to "remoteConfig"
    )
}