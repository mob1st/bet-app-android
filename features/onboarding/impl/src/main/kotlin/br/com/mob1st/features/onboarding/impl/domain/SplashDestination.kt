package br.com.mob1st.features.onboarding.impl.domain

import br.com.mob1st.features.auth.publicapi.domain.AuthStatus

sealed class SplashDestination {

    object Home : SplashDestination()

    object Onboarding : SplashDestination()

    companion object {
        fun of(authStatus: AuthStatus): SplashDestination {
            return if (authStatus.hasAccess) {
                Home
            } else {
                Onboarding
            }
        }
    }
}
