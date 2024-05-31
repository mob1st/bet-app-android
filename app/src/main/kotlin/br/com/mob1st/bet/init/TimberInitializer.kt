package br.com.mob1st.bet.init

import android.content.Context
import androidx.startup.Initializer
import br.com.mob1st.bet.BuildConfig
import br.com.mob1st.libs.firebase.crashlytics.FirebaseTimberTree
import timber.log.Timber

class TimberInitializer : Initializer<Timber.Forest> {
    override fun create(context: Context): Timber.Forest {
        return if (BuildConfig.DEBUG) {
            Timber.DebugTree()
        } else {
            FirebaseTimberTree()
        }.let {
            Timber.plant(it)
            Timber
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(FirebaseInitializer::class.java)
    }
}
