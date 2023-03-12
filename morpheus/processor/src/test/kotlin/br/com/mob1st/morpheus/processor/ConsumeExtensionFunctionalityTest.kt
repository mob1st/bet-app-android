package br.com.mob1st.morpheus.processor

import br.com.mob1st.morpheus.annotation.Consumable
import br.com.mob1st.morpheus.processor.utils.compilation
import br.com.mob1st.morpheus.processor.utils.createInstance
import br.com.mob1st.morpheus.processor.utils.shouldExitOk
import com.tschuchort.compiletesting.SourceFile
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.lang.reflect.Modifier

class ConsumeExtensionFunctionalityTest : FunSpec({

    val compilation = compilation(
        SourceFile.kotlin(
            "Sample.kt",
            """               
                import br.com.mob1st.morpheus.annotation.ConsumableEffect
                import br.com.mob1st.morpheus.annotation.Morpheus
                @Morpheus
                data class Sample(
                    @ConsumableEffect
                    val field1: String? = "value1",
                    @ConsumableEffect
                    val field2: String? = "value2"
                )                    
            """.trimIndent()
        )
    )
    test("consume method is calling the proper field") {
        val result = compilation.compile()
        result.shouldExitOk()
        val extensionClass = result.classLoader.loadClass("SampleEffectKeyKt")
        val obj = result.classLoader.createInstance("Sample")
        val field1Return = extensionClass.callConsumeFunction(obj, "Field1")
        val field2Return = extensionClass.callConsumeFunction(obj, "Field2")
        field1Return shouldBe "value1"
        field2Return shouldBe "value2"
    }
})

private fun Class<*>.callConsumeFunction(obj: Any, fieldName: String): String {
    val consumeExtension = methods.first {
        it.name == "consume$fieldName" && Modifier.isStatic(it.modifiers)
    }

    @Suppress("UNCHECKED_CAST")
    val consumable = consumeExtension.invoke(obj, obj) as Consumable<*, String>
    return consumable.effect
}
