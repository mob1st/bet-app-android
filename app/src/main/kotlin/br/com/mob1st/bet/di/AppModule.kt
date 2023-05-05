package br.com.mob1st.bet.di

import android.app.Application
import br.com.mob1st.bet.BetApp
import br.com.mob1st.libs.firebase.firestore.defaultJson
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
    fun json() = defaultJson

    @Single
    @Named("io")
    fun dispatcherIO() = Dispatchers.IO

    @Single
    @Named("main")
    fun dispatcherMain() = Dispatchers.Main

    @Factory
    @Named("appScope")
    fun appScope(application: Application) = (application as BetApp).appScope
}
