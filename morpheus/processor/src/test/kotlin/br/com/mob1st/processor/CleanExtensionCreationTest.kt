package br.com.mob1st.processor

import br.com.mob1st.morpheus.annotation.Consumable
import br.com.mob1st.processor.utils.compile
import br.com.mob1st.processor.utils.constructor
import br.com.mob1st.processor.utils.givenMorpheusClass
import br.com.mob1st.processor.utils.property
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.jetbrains.kotlin.descriptors.runtime.structure.parameterizedTypeArguments
import java.lang.reflect.Modifier

private const val FILE_NAME = "Sample.kt"
class CleanExtensionCreationTest : BehaviorSpec({

    Given("annotated class with annotated property") {
        val morpheusClass = givenMorpheusClass("Sample")
        val prop1 = property(
            """
                @ConsumableEffect val prop1: Int? = null
            """.trimIndent()
        )

        When("compile") {
            val result = morpheusClass.constructor(prop1).compile(FILE_NAME)
            val generatedClass = result.classLoader.loadClass("SampleEffectKeyKt")
            val clearEffects = generatedClass.methods.filter {
                it.name == "clearEffect" && Modifier.isStatic(it.modifiers)
            }
            And("get the generated extension") {
                val extension = clearEffects.first()
                Then("an extension should be created with name clearEffect") {
                    clearEffects.size shouldBe 1
                }
                And("get the parameters") {

                    val parameters = extension.parameters

                    Then("the parameter count should be 1") {
                        // we should count the receiver
                        parameters.size shouldBe 2
                    }
                    And("get the parameter") {
                        val parameter = parameters.last()

                        And("get its type") {
                            Then("its type should be Consumable") {
                                parameter.type shouldBe Consumable::class.java
                            }
                        }
                        And("get parameterized types of parameter") {
                            val parameterizedTypes = parameter
                                .parameterizedType
                                .parameterizedTypeArguments
                            And("get the first type") {
                                Then("it should be the genereted enum") {
                                    parameterizedTypes[0].typeName shouldBe "SampleEffectKey"
                                }
                            }
                            And("get the second type") {
                                Then("it should be a wildcard") {
                                    // wildcard is * in kotlin and ? in java
                                    parameterizedTypes[1].typeName shouldBe "?"
                                }
                            }
                        }
                    }
                }
                And("get the return type") {
                    val returnType = extension.returnType
                    Then("it should be the receiver class") {
                        returnType.simpleName shouldBe "Sample"
                    }
                }
            }
        }
    }
})
