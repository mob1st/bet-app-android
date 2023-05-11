package br.com.mob1st.features.onboarding.impl.domain

import br.com.mob1st.features.auth.publicapi.domain.AuthStatus
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class SplashDestinationTest : BehaviorSpec({

    Given("a logged in status") {
        val authStatus = AuthStatus.LOGGED_IN
        When("get destination") {
            val destination = SplashDestination.of(authStatus)
            Then("should be home") {
                destination shouldBe SplashDestination.Home
            }
        }
    }

    Given("an anonymous status") {
        val authStatus = AuthStatus.ANONYMOUS
        When("get destination") {
            val destination = SplashDestination.of(authStatus)
            Then("should be home") {
                destination shouldBe SplashDestination.Home
            }
        }
    }

    Given("a logged out status") {
        val authStatus = AuthStatus.LOGGED_OUT
        When("get destination") {
            val destination = SplashDestination.of(authStatus)
            Then("should be onboarding") {
                destination shouldBe SplashDestination.Onboarding
            }
        }
    }
})
