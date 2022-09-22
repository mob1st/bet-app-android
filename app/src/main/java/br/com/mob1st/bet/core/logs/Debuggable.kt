package br.com.mob1st.bet.core.logs

/**
 * Provides log properties to be displayed in the logs tools
 */
interface Debuggable {

    /**
     * Key-value map used top log properties by keys
     *
     * Try to log only primitive values if possible, or another instance of [Debuggable]
     * Otherwise the third-party log tool used in this project will log the toString of the value.
     *
     * AVOID LOG SENSITIVE USER INFORMATION
     *
     * @return a map containing the keys and the values to be logged
     */
    fun logProperties(): Map<String, Any?>

}