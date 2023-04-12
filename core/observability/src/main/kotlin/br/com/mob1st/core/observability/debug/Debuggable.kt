package br.com.mob1st.core.observability.debug

/**
 * Provide properties to be debuggable on logs
 */
interface Debuggable {

    /**
     * Map of properties to be logged.
     *
     * Be aware of the types of the values. They can change depending on the log tool used so try to be simple.
     * Prefer simple types like primitives, Dates, Strings, etc.
     * It should be used for facilitate observability and debugging, so try to make it simple and valuable.
     */
    val logInfo: Map<String, Any?>
}
