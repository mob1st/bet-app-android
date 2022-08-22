package br.com.mob1st.bet.features.auth

import android.util.Log

class OpenAppUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke() {
        if (!authRepository.hasLoggedUser()) {
            Log.d("ptest", "init login")
            authRepository.signInGuestUser()
        } else {
            Log.d("ptest", "already logged")
        }
    }

}