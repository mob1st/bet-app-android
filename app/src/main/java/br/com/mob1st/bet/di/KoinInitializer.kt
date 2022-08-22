package br.com.mob1st.bet.di

import android.content.Context
import androidx.startup.Initializer
import org.koin.core.KoinApplication

@Suppress("unused")
class KoinInitializer : Initializer<KoinApplication> {
    override fun create(context: Context): KoinApplication {
        return Injector.init(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}