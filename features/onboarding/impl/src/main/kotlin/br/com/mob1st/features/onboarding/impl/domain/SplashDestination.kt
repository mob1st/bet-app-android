package br.com.mob1st.features.onboarding.impl.domain

sealed class SplashDestination {

    object Home : SplashDestination()

    object Onboarding : SplashDestination()
}
