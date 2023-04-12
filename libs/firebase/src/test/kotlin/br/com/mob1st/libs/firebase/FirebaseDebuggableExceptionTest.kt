package br.com.mob1st.libs.firebase

import io.kotest.core.spec.style.BehaviorSpec

class FirebaseDebuggableExceptionTest : BehaviorSpec({

    Given("a firestore exception") {
        When("get log properties") {
            Then("name and code should be loggable") {
                // TODO
            }
        }
    }
})
