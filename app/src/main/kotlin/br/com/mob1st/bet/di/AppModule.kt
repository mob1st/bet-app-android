package br.com.mob1st.bet.di

import android.app.Application
import br.com.mob1st.core.kotlinx.coroutines.APP_SCOPE
import br.com.mob1st.core.kotlinx.coroutines.AppScopeProvider
import br.com.mob1st.core.kotlinx.coroutines.DEFAULT
import br.com.mob1st.core.kotlinx.coroutines.IO
import br.com.mob1st.core.kotlinx.coroutines.MAIN
import br.com.mob1st.core.kotlinx.serialization.defaultJson
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
@ComponentScan("br.com.mob1st.bet")
class AppModule {

    @Single
    fun json() = defaultJson()

    @Single
    @Named(IO)
    fun dispatcherIO() = Dispatchers.IO

    @Single
    @Named(MAIN)
    fun dispatcherMain() = Dispatchers.Main

    @Single
    @Named(DEFAULT)
    fun dispatcherDefault() = Dispatchers.Default

    @Factory
    @Named(APP_SCOPE)
    fun appScope(application: Application) = (application as AppScopeProvider).appScope
}
