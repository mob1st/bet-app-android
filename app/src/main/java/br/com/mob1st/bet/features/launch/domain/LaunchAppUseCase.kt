package br.com.mob1st.bet.features.launch.domain

import br.com.mob1st.bet.core.analytics.AnalyticsTool
import br.com.mob1st.bet.core.coroutines.DispatcherProvider
import br.com.mob1st.bet.core.localization.default
import br.com.mob1st.bet.core.logs.CrashReportingTool
import br.com.mob1st.bet.core.logs.Logger
import br.com.mob1st.bet.features.competitions.CompetitionSubscribeEvent
import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry
import br.com.mob1st.bet.features.competitions.domain.CompetitionRepository
import br.com.mob1st.bet.features.ff.FeatureFlagRepository
import br.com.mob1st.bet.features.profile.SignInEvent
import br.com.mob1st.bet.features.profile.domain.AuthMethod
import br.com.mob1st.bet.features.profile.domain.LoggedOut
import br.com.mob1st.bet.features.profile.domain.User
import br.com.mob1st.bet.features.profile.domain.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

/**
 * Handles the app launching action and provides the [CompetitionEntry] to be used when the user is
 * moved to the home
 */
@Factory
class LaunchAppUseCase(
    private val userRepository: UserRepository,
    private val featureFlagRepository: FeatureFlagRepository,
    private val competitionRepository: CompetitionRepository,
    private val analyticsTool: AnalyticsTool,
    private val crashReportingTool: CrashReportingTool,
    private val provider: DispatcherProvider,
    private val logger: Logger,
) {

    private val default get() = provider.default

    suspend operator fun invoke(): CompetitionEntry = withContext(default) {
        val responses = awaitAll(
            async { featureFlagRepository.sync() },
            async { getUser() }
        )
        logger.i("sync the feature flags and retrieve the user")
        val user = responses.last() as User
        if (user.activeSubscriptions == 0 && usesDefaultCompetition()) {
            logger.i("subscribe the user in the default competition")
            subscribeInDefaultCompetition()
        } else {
            logger.i("get first available competition")
            userRepository.getFirstAvailableSubscription()
        }
    }

    private suspend fun getUser() = if (userRepository.getAuthStatus() is LoggedOut) {
        logger.v("user is logged out. do the login")
        registerUser()
    } else {
        logger.v("user is already authenticated")
        userRepository.get()
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

    private suspend fun subscribeInDefaultCompetition(): CompetitionEntry {
        logger.v("get the default competition")
        val defaultCompetition = competitionRepository.getDefaultCompetition()
        val entry = defaultCompetition.toEntry()
        logger.v("subscribe the user into competition ${entry.name.default}")
        userRepository.subscribe(entry)
        analyticsTool.log(
            CompetitionSubscribeEvent(
                entry = entry,
                method = CompetitionSubscribeEvent.Method.AUTOMATIC
            )
        )
        return entry
    }

    companion object {
        private const val FF_DEFAULT_COMPETITION = "ff_default_competition"
    }

}