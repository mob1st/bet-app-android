package br.com.mob1st.processor

import com.tschuchort.compiletesting.KotlinCompilation
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

private const val FILE_NAME = "Expected.kt"
private const val GENERATED_CLASS_NAME = "MorpheusSampleKey"

class EnumCreationTest : BehaviorSpec({
    Given("a data class annotated with @Morpheus") {
        val dataClass = givenClass("Sample", modifier = ClassModifier.DATA)

        And("one nullable property is annotated with @ConsumableEffect") {
            val prop1 = property(
                """
                @ConsumableEffect val prop1: String?
                """.trimIndent()
            )

            When("compile") {
                val result = dataClass.constructor(prop1).compile(FILE_NAME)
                Then("the generated enum constant should be the expected") {
                    result.exitCode shouldBe KotlinCompilation.ExitCode.OK
                    val generatedEnum = result.classLoader.loadClass(GENERATED_CLASS_NAME)
                    generatedEnum.enumConstants.size shouldBe 1
                    generatedEnum.enumConstants.first().toString() shouldBe "Prop1"
                }
            }
        }

        And("two properties is annotated with @ConsumableEffect") {
            val prop1 = property(
                """
                @ConsumableEffect val prop1: String?
                """.trimIndent()
            )
            val prop2 = property(
                """
                @ConsumableEffect val prop2: Int?
                """.trimIndent()
            )

            When("compile") {
                val result = dataClass.constructor(prop1, prop2).compile(FILE_NAME)
                Then("the names of the enum constants should be the same than the properties but capitalized") {
                    result.exitCode shouldBe KotlinCompilation.ExitCode.OK
                    val generatedEnum = result.classLoader.loadClass(GENERATED_CLASS_NAME)
                    generatedEnum.enumConstants.size shouldBe 2
                    generatedEnum.enumConstants[0].toString() shouldBe "Prop1"
                    generatedEnum.enumConstants[1].toString() shouldBe "Prop2"
                }
            }
        }

        And("one nullable property is annotated with @ConsumableEffect and other don't") {
            When("compile") {
                Then("the generated enum constant should be only for the annotated one") {
                }
            }
        }

        And("one non nullable property is annotated with @ConsumableEffect") {
            When("compile") {
                Then("should fail") {
//                    shouldThrow<IllegalStateException> {
//
//                    }
                }
            }
        }

        And("no property is annotated with @ConsumableEffect") {
            When("compile") {
                Then("no class should be generated") {
                }
            }
        }
    }

    Given("a data class without @Morpheus annotation") {
        When("compile") {
            Then("no class should be generated And a log message should be present") {
            }
        }
    }

    Given("a non data class with @Morpheus annotation") {
        When("compile") {
            Then("no class should be generated And a log message should be present") {
            }
        }
    }
})
