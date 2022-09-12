package br.com.mob1st.bet.features.launch

import br.com.mob1st.bet.core.analytics.AnalyticsTool
import br.com.mob1st.bet.core.coroutines.DispatcherProvider
import br.com.mob1st.bet.core.localization.default
import br.com.mob1st.bet.core.logs.CrashReportingTool
import br.com.mob1st.bet.features.competitions.CompetitionSubscriptionEvent
import br.com.mob1st.bet.features.competitions.domain.CompetitionRepository
import br.com.mob1st.bet.features.ff.FeatureFlagRepository
import br.com.mob1st.bet.features.profile.domain.AuthMethod
import br.com.mob1st.bet.features.profile.domain.LoggedOut
import br.com.mob1st.bet.features.profile.domain.User
import br.com.mob1st.bet.features.profile.domain.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class LaunchAppUseCase(
    private val userRepository: UserRepository,
    private val featureFlagRepository: FeatureFlagRepository,
    private val competitionRepository: CompetitionRepository,
    private val analyticsTool: AnalyticsTool,
    private val crashReportingTool: CrashReportingTool,
    private val provider: DispatcherProvider,
) {

    private val default get() = provider.default

    suspend operator fun invoke() = withContext(default) {
        val responses = awaitAll(
            async {
                featureFlagRepository.sync()
            },
            async {
                if (userRepository.getAuthStatus() is LoggedOut) {
                    registerUser()
                } else {
                    userRepository.get()
                }
            }
        )
        val user = responses.last() as User
        if (user.activeSubscriptions == 0 && usesDefaultCompetition()) {
            subscribeInDefaultCompetition()
        }
    }

    private fun usesDefaultCompetition(): Boolean {
        return featureFlagRepository.getBoolean(FF_DEFAULT_COMPETITION)
    }

    private suspend fun registerUser(): User {
        val user = userRepository.signInAnonymously()
        analyticsTool.registerUser(user.id)
        crashReportingTool.registerUser(user.id)
        analyticsTool.log(SignInEvent(AuthMethod.ANONYMOUS))
        return user
    }

    private suspend fun subscribeInDefaultCompetition() {
        val defaultCompetition = competitionRepository.getDefaultCompetition()
        userRepository.subscribe(defaultCompetition)
        analyticsTool.log(
            CompetitionSubscriptionEvent(
                competitionId = defaultCompetition.id,
                competitionName = defaultCompetition.name.default,
                method = CompetitionSubscriptionEvent.Method.AUTOMATIC
            )
        )
    }

    companion object {
        private const val FF_DEFAULT_COMPETITION = "ff_default_competition"
    }

}