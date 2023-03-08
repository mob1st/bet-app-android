package br.com.mob1st.processor

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.intellij.lang.annotations.Language

private const val FILE_NAME = "AnyName.kt"
private const val EXPECTED_FILE_NAME = "SampleEffectKey"

class FileCreationTest : BehaviorSpec({
    Given("a data class annotated with @Morpheus") {
        val dataClass = givenMorpheusClass("Sample")

        And("a nullable property is annotated with @ConsumableEffect") {
            val prop = property(
                """
                @ConsumableEffect val prop: String?
                """.trimIndent()
            )

            When("compile") {
                val result = dataClass.constructor(prop).compile(FILE_NAME)
                Then("result should be ok and file should be created") {
                    result.shouldExitOk()
                    result.generatedFiles.any {
                        it.name == "$EXPECTED_FILE_NAME.class"
                    } shouldBe true
                    result.generatedFiles.any {
                        it.name == "${EXPECTED_FILE_NAME}Kt.class"
                    } shouldBe true
                }
            }
        }

        And("a non nullable property is annotated with @ConsumableEffect") {
            val prop = property(
                """
                @ConsumableEffect val prop: String
                """.trimIndent()
            )
            When("compile") {
                val result = dataClass.constructor(prop).compile(FILE_NAME)
                Then("compilation should exit with error") {
                    result.shouldExitWithCompilationError()
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
                    result.shouldExitWithCompilationError()
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
            Then("no file should be generated And a log message should be present") {
                result.shouldExitOk()
                result.generatedFiles.any {
                    it.name == "$EXPECTED_FILE_NAME.class"
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
                @ConsumableEffect val prop1: String?            
            )
        """.trimIndent()

        When("compile") {
            val result = clazz.compile(FILE_NAME)
            Then("compilation should fail") {
                result.shouldExitWithCompilationError()
            }
        }
    }
})
