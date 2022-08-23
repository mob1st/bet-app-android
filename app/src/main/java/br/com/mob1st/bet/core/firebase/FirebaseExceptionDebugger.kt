package br.com.mob1st.bet.core.firebase

import br.com.mob1st.bet.core.logs.ThrowableDebugger
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException

object FirebaseExceptionDebugger : ThrowableDebugger {

    override fun propertiesFrom(throwable: Throwable): Map<String, Any> {
        return when(throwable) {
            is FirebaseFirestoreException -> throwable.loggedProperties()
            is FirebaseAuthException -> throwable.loggedProperties()
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

fun FirebaseAuthException.loggedProperties(): Map<String, Any> {
    return mapOf(
        "errorCode" to errorCode,
        "errorSource" to "authentication"
    )
}