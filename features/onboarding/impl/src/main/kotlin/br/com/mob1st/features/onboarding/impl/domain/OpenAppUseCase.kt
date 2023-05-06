package br.com.mob1st.features.onboarding.impl.domain

import br.com.mob1st.core.kotlinx.coroutines.DEFAULT
import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.auth.publicapi.domain.AuthRepository
import br.com.mob1st.features.auth.publicapi.domain.AuthStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import timber.log.Timber

@Factory
class OpenAppUseCase(
    @Named(DEFAULT)
    private val default: CoroutineDispatcher,
    private val authRepository: AuthRepository,
    private val analyticsReporter: AnalyticsReporter,
) {

    operator fun invoke(): Flow<SplashDestination> {
        Timber.i("User has access, going to home")
        analyticsReporter.log(OpenAppAnalyticsEvent())
        return authRepository.authStatus
            .map(::getNextStep)
            .flowOn(default)
    }

    private fun getNextStep(authStatus: AuthStatus): SplashDestination {
        return if (authStatus.hasAccess) {
            Timber.d("User has access, going to home")
            SplashDestination.Home
        } else {
            Timber.d("User has no access, going to onboarding")
            SplashDestination.Onboarding
        }
    }
}
