package br.com.mob1st.bet.init

import android.content.Context
import androidx.startup.Initializer
import br.com.mob1st.bet.BuildConfig
import br.com.mob1st.bet.core.leak.LeakLoggerListener
import leakcanary.LeakCanary

class LeakInitializer : Initializer<LeakCanary> {
    override fun create(context: Context): LeakCanary {
        if (!BuildConfig.DEBUG) {
            LeakCanary.config = LeakCanary.config.run {
                copy(eventListeners = eventListeners + LeakLoggerListener)
            }
        }
        return LeakCanary
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(TimberInitializer::class.java)
    }
}
