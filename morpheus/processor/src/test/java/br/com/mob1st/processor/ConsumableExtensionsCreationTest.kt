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
private const val GENERATED_EXTENSION_CLASS_NAME = "SampleEffectKeyKt"

class ConsumableExtensionsCreationTest : BehaviorSpec({
    val morpheusClass = givenMorpheusClass("Sample")
    val prop1 = property(
        """
        @ConsumableEffect val prop1: Int? = null
        """.trimIndent()
    )

    Given("one annotated property") {
        When("compile") {
            val result = morpheusClass.constructor(prop1).compile(FILE_NAME)
            val generated = result.classLoader.loadClass(GENERATED_EXTENSION_CLASS_NAME)
            val extensions = generated.methods.filter {
                it.name.startsWith("consume") && Modifier.isStatic(it.modifiers)
            }
            Then("two extension functions should be created") {
                extensions.size shouldBe 1
            }
            And("get the extension") {
                val extension = extensions.first()
                Then("the extension name should be consume + the property name") {
                    extension.name shouldBe "consumeProp1"
                }
                Then("the extension parameters should be 0") {
                    // since it's java code the first parameter of a extension function
                    // is the receiver type itself. So we can ignore it
                    extension.parameterCount shouldBe 1
                }
                And("get the return type") {
                    val returnType = extension.returnType
                    Then("the return type should be consumable") {
                        returnType shouldBe Consumable::class.java
                    }
                }
                And("get the parameterized types of the return") {
                    val returnGenericType = extension.genericReturnType.parameterizedTypeArguments
                    Then("the first type should be the generated enum class") {
                        returnGenericType[0].typeName shouldBe "SampleEffectKey"
                    }
                    Then("the second type should be the property type") {
                        returnGenericType[1] shouldBe Int::class.javaObjectType
                    }
                }
            }
        }
    }

    Given("two annotated properties") {
        val prop2 = property(
            """
            @ConsumableEffect val prop2: String? = null
            """.trimIndent()
        )

        When("compile") {
            val result = morpheusClass.constructor(prop1, prop2).compile(FILE_NAME)
            val generated = result.classLoader.loadClass(GENERATED_EXTENSION_CLASS_NAME)
            val extensions = generated.methods.filter {
                it.name.startsWith("consume") && Modifier.isStatic(it.modifiers)
            }
            Then("two extension functions should be created") {
                extensions.size shouldBe 2
            }

            Then("the generated consumable functions should be named with the properties names") {
                extensions[0].name shouldBe "consumeProp1"
                extensions[1].name shouldBe "consumeProp2"
            }
        }
    }

    Given("two properties but only one annotated") {
        val prop2 = property(
            """
            val prop2: String? = null
            """.trimIndent()
        )

        When("compile") {
            val result = morpheusClass.constructor(prop1, prop2).compile(FILE_NAME)
            val generated = result.classLoader.loadClass(GENERATED_EXTENSION_CLASS_NAME)
            val extensions = generated.methods.filter {
                it.name.startsWith("consume") && Modifier.isStatic(it.modifiers)
            }
            Then("only one extension should be created") {
                extensions.size shouldBe 1
            }
        }
    }
})
