package br.com.mob1st.bet.core.logs

interface Logger {

    fun v(message: String, throwable: Throwable? = null, vararg params: Any?)
    fun d(message: String, throwable: Throwable? = null, vararg params: Any?)
    fun i(message: String, throwable: Throwable? = null, vararg params: Any?)
    fun w(message: String, throwable: Throwable? = null, vararg params: Any?)
    fun e(message: String, throwable: Throwable, vararg params: Any?)
}