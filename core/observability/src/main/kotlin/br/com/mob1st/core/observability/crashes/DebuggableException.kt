package br.com.mob1st.core.observability.crashes

import br.com.mob1st.core.observability.debug.Debuggable

private const val MAX_CAUSE_DEEP = 3

/**
 * Get the log properties of this exception and its causes, if they somehow implements [Debuggable].
 *
 * It tries to get a maximum of 5 causes, and if repeated keys are found, only the last one is used.
 */
fun Throwable.getRootLogProperties(): Map<String, Any?> = getRootLogProperties(0)

private fun Throwable?.getRootLogProperties(currentDeepIndex: Int): Map<String, Any?> {
    return when {
        this == null -> emptyMap()
        this is Debuggable -> {
            val causeProperties = cause.getRootLogProperties(currentDeepIndex + 1)
            causeProperties + logInfo
        }
        currentDeepIndex < MAX_CAUSE_DEEP -> cause.getRootLogProperties(currentDeepIndex + 1)
        else -> emptyMap()
    }
}
