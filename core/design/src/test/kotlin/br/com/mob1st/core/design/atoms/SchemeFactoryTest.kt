package br.com.mob1st.core.design.atoms

import br.com.mob1st.core.design.atoms.colors.AppSchemeFactory
import io.kotest.core.spec.style.BehaviorSpec

class SchemeFactoryTest : BehaviorSpec({
    Given("a light theme") {
        val isDark = false
        When("create") {
            val actual = AppSchemeFactory.create(isDark)
            Then("actual should be the expected light theme") {
                TODO()
            }
        }
    }
}) {
    private companion object Expected {
    }
}
