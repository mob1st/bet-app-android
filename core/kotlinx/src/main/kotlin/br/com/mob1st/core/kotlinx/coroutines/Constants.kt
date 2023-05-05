package br.com.mob1st.core.kotlinx.coroutines

/**
 * Can be used in qualifier annotations to identify the instance of [kotlinx.coroutines.Dispatchers.IO]
 */
const val IO = "br.com.mob1st.core.kotlinx.coroutines.IO"

/**
 * Can be used in qualifier annotations to identify the instance of [kotlinx.coroutines.Dispatchers.Main]
 */
const val MAIN = "br.com.mob1st.core.kotlinx.coroutines.MAIN"

/**
 * Can be used in qualifier annotations to identify the instance of [kotlinx.coroutines.Dispatchers.Default]
 */
const val DEFAULT = "br.com.mob1st.core.kotlinx.coroutines.DEFAULT"

/**
 * Can be used in qualifier annotations to identify the instance of [kotlinx.coroutines.CoroutineScope] attached into
 * the App lifecycle
 */
const val APP_SCOPE = "br.com.mob1st.core.kotlinx.coroutines.APP_SCOPE"
