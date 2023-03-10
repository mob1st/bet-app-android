package br.com.mob1st.processor

import br.com.mob1st.morpheus.annotation.Consumable
import br.com.mob1st.processor.utils.compilation
import br.com.mob1st.processor.utils.createInstance
import br.com.mob1st.processor.utils.shouldExitOk
import com.tschuchort.compiletesting.SourceFile
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.lang.reflect.Modifier

class ConsumeExtensionFunctionalityTest : FunSpec({

    test("consume method is calling the proper field") {
        val result = compilation(
            SourceFile.kotlin(
                "Sample.kt",
                """
                
                import br.com.mob1st.morpheus.annotation.ConsumableEffect
                import br.com.mob1st.morpheus.annotation.Morpheus
                @Morpheus
                data class Sample(
                    @ConsumableEffect
                    val field: String? = "value"
                )                    
                """.trimIndent()
            )
        ).compile()
        result.shouldExitOk()
        val extensionClass = result.classLoader.loadClass("SampleEffectKeyKt")

        val obj = result.classLoader.createInstance("Sample")

        val consumeExtension = extensionClass.methods.first {
            it.name == "consumeField" && Modifier.isStatic(it.modifiers)
        }

        @Suppress("UNCHECKED_CAST")
        val consumable = consumeExtension.invoke(obj, obj) as Consumable<*, String>
        consumable.effect shouldBe "value"
    }
})
