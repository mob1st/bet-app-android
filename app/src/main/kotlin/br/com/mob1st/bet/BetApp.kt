package br.com.mob1st.bet

import android.app.Application
import br.com.mob1st.core.kotlinx.coroutines.AppCoroutineScope
import br.com.mob1st.core.kotlinx.coroutines.AppScopeProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

class BetApp : Application(), AppScopeProvider {
    private lateinit var _appScope: CoroutineScope
    override val appScope: AppCoroutineScope get() = _appScope as AppCoroutineScope

    override fun onCreate() {
        _appScope = AppCoroutineScope()
        super.onCreate()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        _appScope.cancel("onLowMemory called by the BetApp")
        _appScope = AppCoroutineScope()
    }
}
