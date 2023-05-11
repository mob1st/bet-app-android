package br.com.mob1st.features.onboarding.impl.domain

import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.auth.publicapi.domain.AuthRepository
import br.com.mob1st.features.auth.publicapi.domain.AuthStatus
import br.com.mob1st.features.dev.publicapi.domain.FeatureFlagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.testCoroutineScheduler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
class OpenAppUseCaseTest : BehaviorSpec({
    Given("an authentication status") {
        val authRepository = mockk<AuthRepository>()
        val analyticsReporter = mockk<AnalyticsReporter>()
        val featureFlagRepository = mockk<FeatureFlagRepository>()
        every { authRepository.authStatus } returns flowOf(AuthStatus.LOGGED_IN)
        every { analyticsReporter.log(any()) } just runs
        coEvery { featureFlagRepository.sync() } just runs
        val useCase = OpenAppUseCase(
            default = UnconfinedTestDispatcher(testCoroutineScheduler),
            analyticsReporter = analyticsReporter,
            authRepository = authRepository,
            featureFlagRepository = featureFlagRepository
        )
        When("open the app") {
            launch {
                useCase().first()
            }
            testCoroutineScheduler.runCurrent()
            Then("the user should be redirected to the home screen") {
                verify { authRepository.authStatus }
            }
            Then("open app event should be logged") {
                verify { analyticsReporter.log(OpenAppAnalyticsEvent) }
            }

            Then("feature flag should be synced") {
                coVerify { featureFlagRepository.sync() }
            }
        }
    }
})
