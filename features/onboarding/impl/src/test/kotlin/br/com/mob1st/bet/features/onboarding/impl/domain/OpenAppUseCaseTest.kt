package br.com.mob1st.bet.features.onboarding.impl.domain

import io.kotest.core.spec.style.BehaviorSpec

class OpenAppUseCaseTest : BehaviorSpec({

    Given("a logged user") {
        When("the app opens") {
            Then("the user should be redirected to the home screen") {
            }

            Then("load the features flag") {
            }
        }
    }

    Given("non user logged") {
        When("the app opens") {
            Then("the user should be redirected to the onboarding screen") {
            }
            Then("load the features flag") {
            }
        }
    }
})
