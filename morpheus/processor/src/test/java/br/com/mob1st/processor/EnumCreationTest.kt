package br.com.mob1st.processor

import com.tschuchort.compiletesting.KotlinCompilation
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.intellij.lang.annotations.Language

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
            val prop1 = property(
                """
                @ConsumableEffect val prop1: String?
                """.trimIndent()
            )
            val prop2 = property(
                """
                val prop2: Int?
                """.trimIndent()
            )

            When("compile") {
                val result = dataClass.constructor(prop1, prop2).compile(FILE_NAME)
                Then("the generated enum constant should be only for the annotated one") {
                    result.exitCode shouldBe KotlinCompilation.ExitCode.OK
                    val generatedEnum = result.classLoader.loadClass(GENERATED_CLASS_NAME)
                    generatedEnum.enumConstants.size shouldBe 1
                    generatedEnum.enumConstants[0].toString() shouldBe "Prop1"
                }
            }
        }

        And("one non nullable property is annotated with @ConsumableEffect") {
            val prop1 = property(
                """
                @ConsumableEffect val prop1: String
                """.trimIndent()
            )
            When("compile") {
                val result = dataClass.constructor(prop1).compile(FILE_NAME)
                Then("compilation should fail") {
                    result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
                }
            }
        }

        And("no property is annotated with @ConsumableEffect") {
            val prop = property(
                """
                val prop1: String?
                """.trimIndent()
            )
            When("compile") {
                val result = dataClass.constructor(prop).compile(FILE_NAME)
                Then("compilation should fail") {
                    result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
                }
            }
        }
    }

    Given("a data class without @Morpheus annotation") {
        @Language("kotlin")
        val clazz = """           
            import br.com.mob1st.morpheus.annotation.ConsumableEffect
            data class Sample(
                @ConsumableEffect val prop1: String            
            )
        """.trimIndent()
        When("compile") {
            val result = clazz.compile(FILE_NAME)
            Then("no class should be generated And a log message should be present") {
                result.exitCode shouldBe KotlinCompilation.ExitCode.OK
                result.generatedFiles.any {
                    it.name == "$GENERATED_CLASS_NAME.class"
                } shouldBe false
            }
        }
    }

    Given("a non data class with @Morpheus annotation") {
        @Language("kotlin")
        val clazz = """           
            import br.com.mob1st.morpheus.annotation.ConsumableEffect
            import br.com.mob1st.morpheus.annotation.Morpheus
            
            @Morpheus
            class Sample(
                @ConsumableEffect val prop1: String            
            )
        """.trimIndent()

        When("compile") {
            val result = clazz.compile(FILE_NAME)
            Then("compilation should fail") {
                result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
            }
        }
    }
})
