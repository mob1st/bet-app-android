package br.com.mob1st.bet.di

import android.content.Context
import br.com.mob1st.libs.firebase.dependencies.FirebaseComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

object Injector {

    fun init(context: Context): KoinApplication {
        return startKoin {
            androidLogger()
            androidContext(context)
            modules(
                // core module
                firebaseModule,
                FirebaseComponent().module,

                coroutinesModule,
                timberModule,
                serializationModule,
                AppModule().module
            )
        }
    }
}
