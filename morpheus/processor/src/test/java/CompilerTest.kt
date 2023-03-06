import br.com.mob1st.processor.MorpheusProcessorProvider
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspWithCompilation
import com.tschuchort.compiletesting.symbolProcessorProviders
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class CompilerTest : FunSpec({

    test("compiler is working") {
        val source = SourceFile.kotlin(
            "Sample.kt",
            """
            import br.com.mob1st.morpheus.annotation.ConsumableEffect
            import br.com.mob1st.morpheus.annotation.Morpheus
            @Morpheus                
            data class Sample(@ConsumableEffect val salame: Int?)
            """.trimIndent()
        )
        val result = compilation(source).compile()

        result.exitCode shouldBe KotlinCompilation.ExitCode.OK
        val clazz = result.classLoader.loadClass("MorpheusSampleKey")
        clazz.isEnum shouldBe true
        clazz.enumConstants.size shouldBe 1
        clazz.enumConstants.first().toString() shouldBe "Salame"
    }
})

fun compilation(vararg sourceFile: SourceFile) = KotlinCompilation().apply {
    symbolProcessorProviders = listOf(MorpheusProcessorProvider())
    sources = sourceFile.asList()
    verbose = false
    kspWithCompilation = true
    inheritClassPath = true
}