package br.com.mob1st.bet.features.ff

import br.com.mob1st.bet.core.coroutines.DispatcherProvider
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
internal class FeatureFlagRepositoryImpl(
    private val remoteConfig: FirebaseRemoteConfig,
    private val provider: DispatcherProvider
) : FeatureFlagRepository {

    private val io get() = provider.io

    override suspend fun sync(): Unit = withContext(io) {
        runCatching {
            remoteConfig.fetchAndActivate().await()
        }.getOrElse {
            throw InitRemoteConfigException(it)
        }
    }

    override fun getBoolean(featureFlag: String): Boolean {
        return remoteConfig[featureFlag].asBoolean()
    }

}

class InitRemoteConfigException(cause: Throwable) : Exception("unable to init the remote config", cause)
