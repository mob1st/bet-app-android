package br.com.mob1st.bet.core.logs

import android.content.Context
import androidx.startup.Initializer
import br.com.mob1st.bet.BuildConfig
import br.com.mob1st.bet.core.firebase.FirebaseInitializer
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import org.koin.core.component.KoinComponent
import timber.log.Timber

class TimberInitializer : Initializer<Timber.Tree>, KoinComponent {

    override fun create(context: Context): Timber.Tree {
        val tree = if (BuildConfig.DEBUG) {
            Timber.DebugTree()
        } else {
            CrashReportingTree(Firebase.crashlytics)
        }
        Timber.plant(tree)
        return Timber.asTree()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(FirebaseInitializer::class.java)
    }

}