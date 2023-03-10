package br.com.mob1st.processor.utils

import br.com.mob1st.processor.MorpheusProcessorProvider
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspWithCompilation
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.intellij.lang.annotations.Language

@Language("kotlin")
internal fun givenMorpheusClass(name: String) =
    """
        import br.com.mob1st.morpheus.annotation.ConsumableEffect
        import br.com.mob1st.morpheus.annotation.Morpheus
        @Morpheus                
        data class $name
    """.trimIndent()

internal fun String.constructor(
    @Language("kotlin")
    vararg properties: String,
) = buildString {
    append("${this@constructor}(")
    properties.forEachIndexed { index, p ->
        append(p)
        if (index < properties.lastIndex) {
            append(",")
        }
    }
    append(")")
}

internal fun property(
    @Language("kotlin")
    valueAndType: String,
) = buildString {
    append("\n")
    append(valueAndType)
    append("\n")
}

internal fun String.compile(fileName: String): KotlinCompilation.Result {
    val source = SourceFile.kotlin(fileName, this)
    return compilation(source).compile()
}

fun compilation(vararg sourceFile: SourceFile) = KotlinCompilation().apply {
    symbolProcessorProviders = listOf(MorpheusProcessorProvider())
    sources = sourceFile.asList()
    verbose = false
    kspWithCompilation = true
    inheritClassPath = true
}
