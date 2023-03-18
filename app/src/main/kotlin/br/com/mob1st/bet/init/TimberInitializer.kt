package br.com.mob1st.bet.init

import android.content.Context
import androidx.startup.Initializer
import br.com.mob1st.bet.BuildConfig
import br.com.mob1st.bet.core.firebase.CrashlyticsTool
import br.com.mob1st.bet.core.timber.CrashReportingTree
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class TimberInitializer : Initializer<Timber.Forest> {
    override fun create(context: Context): Timber.Forest {
        return if (BuildConfig.DEBUG) {
            Timber.DebugTree()
        } else {
            val crt = CrashlyticsTool(FirebaseCrashlytics.getInstance())
            CrashReportingTree(crt)
        }.let {
            Timber.plant(it)
            Timber
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(FirebaseInitializer::class.java)
    }
}
