package br.com.mob1st.morpheus.processor

import br.com.mob1st.morpheus.annotation.Consumable
import br.com.mob1st.morpheus.processor.utils.compilation
import br.com.mob1st.morpheus.processor.utils.createInstance
import br.com.mob1st.morpheus.processor.utils.shouldExitOk
import com.tschuchort.compiletesting.SourceFile
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.lang.reflect.Modifier

class CleanEffectExtensionFunctionalityTest : FunSpec({

    val compilation =
        compilation(
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
                    val field2: String? = "value2",
                )                    
                """.trimIndent(),
            ),
        )

    test("call clean function and check if the field is null") {
        val result = compilation.compile()
        result.shouldExitOk()
        val enumClass = result.classLoader.loadClass("SampleEffectKey")
        val extensionClass = result.classLoader.loadClass("SampleEffectKeyKt")
        val obj = result.classLoader.createInstance("Sample")
        val field1Return =
            extensionClass.callClearEffectFunction(
                obj,
                fieldName = "Field1",
                enumClass = enumClass,
                value = "value1",
            )
        val field2Return =
            extensionClass.callClearEffectFunction(
                obj,
                fieldName = "Field2",
                enumClass = enumClass,
                value = "value2",
            )
        field1Return shouldBe null
        field2Return shouldBe null
    }
})

private fun Class<*>.callClearEffectFunction(
    obj: Any,
    fieldName: String,
    enumClass: Class<*>,
    value: String,
): String? {
    val consumable =
        Consumable(
            enumClass.getField(fieldName).get(null),
            value,
        )
    val clearExtension =
        methods.first {
            it.name == "clearEffect" && Modifier.isStatic(it.modifiers)
        }
    val cleanedObj = clearExtension.invoke(obj, obj, consumable)
    val method = cleanedObj.javaClass.getDeclaredMethod("get$fieldName")
    return method.invoke(cleanedObj) as String?
}
