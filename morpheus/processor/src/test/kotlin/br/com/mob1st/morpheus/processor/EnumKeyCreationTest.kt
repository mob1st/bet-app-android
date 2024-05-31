package br.com.mob1st.morpheus.processor

import br.com.mob1st.morpheus.processor.utils.compile
import br.com.mob1st.morpheus.processor.utils.constructor
import br.com.mob1st.morpheus.processor.utils.givenMorpheusClass
import br.com.mob1st.morpheus.processor.utils.property
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

private const val FILE_NAME = "Sample.kt"
private const val GENERATED_CLASS_NAME = "SampleEffectKey"

class EnumKeyCreationTest : BehaviorSpec({

    val dataClass = givenMorpheusClass("Sample")

    Given("two annotated properties") {
        val prop1 =
            property(
                """
                @ConsumableEffect val prop1: String?
                """.trimIndent(),
            )
        val prop2 =
            property(
                """
                @ConsumableEffect val prop2: String?
                """.trimIndent(),
            )

        When("compile") {
            val result = dataClass.constructor(prop1, prop2).compile(FILE_NAME)
            Then("two keys should be generated") {
                val generatedClass = result.classLoader.loadClass(GENERATED_CLASS_NAME)
                generatedClass.enumConstants.size shouldBe 2
            }
            Then("the enum key name should be the property name capitalized") {
                val generatedClass = result.classLoader.loadClass(GENERATED_CLASS_NAME)
                generatedClass.enumConstants[0].toString() shouldBe "Prop1"
                generatedClass.enumConstants[1].toString() shouldBe "Prop2"
            }
        }
    }

    Given("only one annotated property") {
        val prop1 =
            property(
                """
                @ConsumableEffect val prop1: String?
                """.trimIndent(),
            )
        val prop2 =
            property(
                """
                val prop2: String?
                """.trimIndent(),
            )
        When("compile") {
            val result = dataClass.constructor(prop1, prop2).compile(FILE_NAME)

            Then("an enum with one key should be created") {
                val generatedClass = result.classLoader.loadClass(GENERATED_CLASS_NAME)
                generatedClass.enumConstants.size shouldBe 1
            }
            Then("the enum key name should be the property name capitalized") {
                val generatedClass = result.classLoader.loadClass(GENERATED_CLASS_NAME)
                generatedClass.enumConstants[0].toString() shouldBe "Prop1"
            }
        }
    }
})
