package br.com.mob1st.bet.features.profile

import br.com.mob1st.bet.core.logs.Debuggable

class AnonymousSignInException(
    cause: Throwable,
) : Exception("Unable to sign in the user anonymously", cause)

class UserCreationException(
    private val authId: String,
    cause: Throwable
) : Exception("unable to create the user on db", cause), Debuggable {
    override fun logProperties(): Map<String, Any> {
        return mapOf("authId" to authId)
    }
}

class UserSubscriptionException(
    private val subscriptionId: String,
    cause: Throwable,
) : Exception("unable to subscribe the user in the competition $subscriptionId", cause),
    Debuggable {
    override fun logProperties(): Map<String, Any> {
        return mapOf("subscriptionId" to subscriptionId)
    }
}

class GetUserException(
    cause: Throwable
) : Exception("unable to get the user. check if login was executed first", cause)