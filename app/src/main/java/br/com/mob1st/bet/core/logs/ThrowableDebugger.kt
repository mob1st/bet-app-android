package br.com.mob1st.bet.core.logs

interface ThrowableDebugger {

    fun propertiesFrom(throwable: Throwable): Map<String, Any>

}