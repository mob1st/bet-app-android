package br.com.mob1st.bet.features.launch

import br.com.mob1st.bet.core.analytics.AnalyticsTool
import br.com.mob1st.bet.core.coroutines.DispatcherProvider
import br.com.mob1st.bet.core.logs.CrashReportingTool
import br.com.mob1st.bet.features.competitions.CompetitionRepository
import br.com.mob1st.bet.features.ff.FeatureFlagRepository
import br.com.mob1st.bet.features.profile.AuthMethod
import br.com.mob1st.bet.features.profile.LoggedOut
import br.com.mob1st.bet.features.profile.User
import br.com.mob1st.bet.features.profile.UserRepository
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
    }

    companion object {
        private const val FF_DEFAULT_COMPETITION = "ff_default_competition"
    }

}