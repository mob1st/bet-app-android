package br.com.mob1st.bet

import android.app.Application
import br.com.mob1st.bet.core.coroutines.AppScopeProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

class BetApp : Application(), AppScopeProvider {

    private var _appScope: CoroutineScope = MainScope()
    override val appScope: CoroutineScope get() = _appScope

    override fun onLowMemory() {
        super.onLowMemory()
        _appScope.cancel("onLowMemory called by the BetApp")
        _appScope = MainScope()
    }

}