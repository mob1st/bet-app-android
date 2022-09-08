package br.com.mob1st.bet.core.firebase

import br.com.mob1st.bet.core.logs.ThrowableDebugger
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException

object FirebaseExceptionDebugger : ThrowableDebugger<FirebaseException> {
    override fun propertiesFrom(throwable: FirebaseException): Map<String, Any> {
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

private fun FirebaseAuthException.loggedProperties(): Map<String, Any> {
    return mapOf(
        "errorCode" to errorCode,
        "errorSource" to "authentication"
    )
}