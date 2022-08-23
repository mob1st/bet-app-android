package br.com.mob1st.bet.di

import android.content.Context
import androidx.startup.Initializer
import br.com.mob1st.bet.core.firebase.FirebaseInitializer
import br.com.mob1st.bet.core.timber.TimberInitializer
import org.koin.core.KoinApplication

@Suppress("unused")
class KoinInitializer : Initializer<KoinApplication> {
    override fun create(context: Context): KoinApplication {
        return Injector.init(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(
            TimberInitializer::class.java,
            FirebaseInitializer::class.java
        )
    }
}