package br.com.mob1st.bet.core.exceptions

import br.com.mob1st.bet.core.logs.Debuggable

fun Throwable.getCauseProperties(): Map<String, Any> {
    return when (val cause = cause) {
        is Debuggable -> cause.logProperties()
        null -> emptyMap()
        else -> cause.cause?.getCauseProperties().orEmpty()
    }
}