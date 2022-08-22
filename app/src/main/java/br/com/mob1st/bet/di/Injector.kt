package br.com.mob1st.bet.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

object Injector {

    fun init(application: Application) {
        startKoin {
            androidLogger()
            androidContext(application)
            modules(
                // core module
                firebaseModule,
                coroutinesModule,

                // feature modules
                authModule
            )
        }
    }

}