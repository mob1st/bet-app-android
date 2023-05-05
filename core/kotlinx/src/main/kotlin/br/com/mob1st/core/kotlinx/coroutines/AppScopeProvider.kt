package br.com.mob1st.core.kotlinx.coroutines

import kotlinx.coroutines.CoroutineScope

/**
 * Provides the [CoroutineScope] attached into the App lifecycle
 */
interface AppScopeProvider {

    /**
     * The [CoroutineScope] attached into the App lifecycle
     */
    val appScope: CoroutineScope
}
