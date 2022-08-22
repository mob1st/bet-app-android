package br.com.mob1st.bet.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

object Injector {

    fun init(application: Application) {
        startKoin {
            androidLogger()
            androidContext(application)
            modules(
                // core module
                firebaseModule,
                coroutinesModule,

                AppModule().module
                // feature modules
                //authModule
            )
        }
    }

}