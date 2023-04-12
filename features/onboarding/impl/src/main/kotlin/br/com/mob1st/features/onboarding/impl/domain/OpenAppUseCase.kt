package br.com.mob1st.features.onboarding.impl.domain

import kotlinx.coroutines.delay
import org.koin.core.annotation.Factory
import timber.log.Timber

@Factory
class OpenAppUseCase {

    suspend operator fun invoke(): Destination {
        delay(1)
        Timber.d("empty")
        return Destination.Onboarding
    }

    sealed class Destination {
        object Onboarding : Destination()
        object Home : Destination()
    }
}
