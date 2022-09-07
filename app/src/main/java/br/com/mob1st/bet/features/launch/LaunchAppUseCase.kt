package br.com.mob1st.bet.features.launch

import br.com.mob1st.bet.core.analytics.AnalyticsTool
import br.com.mob1st.bet.core.logs.CrashReportingTool
import br.com.mob1st.bet.features.profile.LoggedOut
import br.com.mob1st.bet.features.profile.User
import br.com.mob1st.bet.features.profile.UserRepository
import org.koin.core.annotation.Factory

@Factory
class LaunchAppUseCase(
    private val userRepository: UserRepository,
    private val analyticsTool: AnalyticsTool,
    private val crashReportingTool: CrashReportingTool
) {

    suspend operator fun invoke() {
        if (userRepository.getAuthStatus() is LoggedOut) {
            val user = userRepository.signInAnonymously()
            registerUser(user)

        }
    }

    private fun registerUser(user: User) {
        analyticsTool.registerUser(user.id)
        crashReportingTool.registerUser(user.id)
    }

}