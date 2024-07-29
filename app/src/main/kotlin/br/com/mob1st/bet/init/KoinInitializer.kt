package br.com.mob1st.bet.init

import android.content.Context
import androidx.startup.Initializer
import br.com.mob1st.bet.di.coroutinesModule
import br.com.mob1st.bet.di.databaseModule
import br.com.mob1st.bet.di.serializationModule
import br.com.mob1st.bet.di.systemModule
import br.com.mob1st.features.finances.impl.dependencies.financesModule
import br.com.mob1st.libs.firebase.dependencies.FirebaseComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin
import org.koin.ksp.generated.module

@Suppress("unused")
class KoinInitializer : Initializer<KoinApplication> {
    override fun create(context: Context): KoinApplication {
        return context.createDependencyGraph()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(
            TimberInitializer::class.java,
            CoilInitializer::class.java,
        )
    }
}

private fun Context.createDependencyGraph() =
    startKoin {
        androidLogger()
        androidContext(this@createDependencyGraph.applicationContext)
        modules(
            FirebaseComponent().module,
            coroutinesModule,
            databaseModule,
            serializationModule,
            financesModule,
            systemModule,
        )
    }
