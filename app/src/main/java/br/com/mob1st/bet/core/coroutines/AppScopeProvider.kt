package br.com.mob1st.bet.core.coroutines

import kotlinx.coroutines.CoroutineScope

/**
 * Make the Application class implements this interface to provide the app coroutine scope
 * It can be useful for cases where suspend functions should be triggered without attach it to the
 * lifecycle of UIs or ViewModels
 */
interface AppScopeProvider {

    /**
     * The coroutine scope attached to the app lifecycle
     */
    val appScope: CoroutineScope

}