package br.com.mob1st.bet.core.logs

/**
 * Used for provides log properties from third-party [Throwable]s
 *
 * In cases you can't make an exception implements the [Debuggable] interface, such as exceptions
 * implemented by frameworks or libraries
 */
interface ThrowableDebugger<T : Throwable> {
    /**
     * For the cases some important properties
     */
    fun propertiesFrom(throwable: T): Map<String, Any>
}
