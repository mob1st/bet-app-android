package br.com.mob1st.morpheus.processor

import br.com.mob1st.morpheus.processor.utils.compile
import br.com.mob1st.morpheus.processor.utils.constructor
import br.com.mob1st.morpheus.processor.utils.givenMorpheusClass
import br.com.mob1st.morpheus.processor.utils.property
import br.com.mob1st.morpheus.processor.utils.shouldExitOk
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

private const val FILE_NAME = "Expected.kt"
private const val GENERATED_CLASS_NAME = "SampleEffectKey"

class EnumCreationTest : BehaviorSpec({

    val subject = givenMorpheusClass("Sample")

    Given("a annotated property") {
        val prop =
            property(
                """
                @ConsumableEffect val prop: String?
                """.trimIndent(),
            )
        When("compile") {
            val result = subject.constructor(prop).compile(FILE_NAME)

            Then("the result should exit ok") {
                result.shouldExitOk()
            }
            Then("a enum class should be created") {
                result.classLoader.loadClass(GENERATED_CLASS_NAME).isEnum shouldBe true
            }
        }
    }
})
