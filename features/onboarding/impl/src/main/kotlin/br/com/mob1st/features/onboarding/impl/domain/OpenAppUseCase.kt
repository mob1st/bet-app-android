package br.com.mob1st.features.onboarding.impl.domain

import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.auth.publicapi.domain.AuthRepository
import br.com.mob1st.features.dev.publicapi.domain.FeatureFlagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import org.koin.core.annotation.Factory
import timber.log.Timber

@Factory
class OpenAppUseCase(
    private val authRepository: AuthRepository,
    private val featureFlagRepository: FeatureFlagRepository,
    private val analyticsReporter: AnalyticsReporter,
) {

    operator fun invoke(): Flow<SplashDestination> {
        Timber.i("User has access, going to home")
        return authRepository.authStatus
            .map(SplashDestination::of)
            .onStart {
                analyticsReporter.log(OpenAppAnalyticsEvent)
                featureFlagRepository.sync()
            }
    }
}
