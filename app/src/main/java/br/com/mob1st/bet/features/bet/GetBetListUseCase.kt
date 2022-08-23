package br.com.mob1st.bet.features.bet

import br.com.mob1st.bet.features.auth.AuthRepository
import br.com.mob1st.bet.features.auth.AuthType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import org.koin.core.annotation.Factory
import java.util.Date

@Factory
class GetBetListUseCase(
    private val authRepository: AuthRepository,
    private val betRepository: BetRepository,
) {

    operator fun invoke(): Flow<List<Guess>> {
        return betRepository.get(Date()).onStart {
            authIfNeeded()
        }
    }

    private suspend fun authIfNeeded() {
        if (authRepository.getAuthType() == AuthType.SignedOut) {
            authRepository.signInGuestUser()
        }
    }

}