package br.com.mob1st.bet.init

import android.content.Context
import androidx.startup.Initializer
import br.com.mob1st.bet.di.Injector
import org.koin.core.KoinApplication

class KoinInitializer : Initializer<KoinApplication> {
    override fun create(context: Context): KoinApplication {
        return Injector.init(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(
            FirebaseInitializer::class.java
        )
    }
}