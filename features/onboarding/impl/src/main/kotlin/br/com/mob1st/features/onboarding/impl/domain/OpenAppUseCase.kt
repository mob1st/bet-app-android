package br.com.mob1st.features.onboarding.impl.domain

import br.com.mob1st.core.kotlinx.coroutines.DEFAULT
import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.auth.publicapi.domain.AuthRepository
import br.com.mob1st.features.dev.publicapi.domain.FeatureFlagRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import timber.log.Timber

@Factory
class OpenAppUseCase(
    @Named(DEFAULT)
    private val default: CoroutineDispatcher,
    private val authRepository: AuthRepository,
    private val analyticsReporter: AnalyticsReporter,
    private val featureFlagRepository: FeatureFlagRepository,
) {

    operator fun invoke(): Flow<SplashDestination> {
        Timber.i("User has access, going to home")
        return authRepository.authStatus
            .map(SplashDestination::of)
            .onStart {
                analyticsReporter.log(OpenAppAnalyticsEvent)
                featureFlagRepository.sync()
            }
            .flowOn(default)
    }
}
