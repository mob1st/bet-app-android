package br.com.mob1st.core.kotlinx.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.plus

/**
 * Provides the [CoroutineScope] attached into the App lifecycle
 */
interface AppScopeProvider {
    /**
     * The [CoroutineScope] attached into the App lifecycle
     */
    val appScope: AppCoroutineScope
}

/**
 * A wrapper to the [CoroutineScope] attached into the App lifecycle.
 */
@JvmInline
value class AppCoroutineScope(
    private val delegated: CoroutineScope = MainScope() + CoroutineName("AppCoroutine"),
) : CoroutineScope by delegated
