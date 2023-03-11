package br.com.mob1st.bet.features.ff

import br.com.mob1st.bet.core.coroutines.DispatcherProvider
import br.com.mob1st.bet.core.firebase.awaitWithTimeout
import br.com.mob1st.bet.core.logs.Logger
import br.com.mob1st.bet.core.utils.functions.suspendRunCatching
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
internal class FeatureFlagRepositoryImpl(
    private val remoteConfig: FirebaseRemoteConfig,
    private val provider: DispatcherProvider,
    private val logger: Logger,
) : FeatureFlagRepository {

    private val io get() = provider.io

    override suspend fun sync(): Unit = withContext(io) {
        suspendRunCatching {
            try {
                remoteConfig.fetchAndActivate().awaitWithTimeout()
            } catch (e: TimeoutCancellationException) {
                logger.w("remote config sync failed with timeout", e)
            }
        }.getOrElse {
            throw InitRemoteConfigException(it)
        }
    }

    override fun getBoolean(featureFlag: String): Boolean {
        return remoteConfig[featureFlag].asBoolean()
    }
}

class InitRemoteConfigException(cause: Throwable) : Exception(
    "unable to init the remote config",
    cause
)
