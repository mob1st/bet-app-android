package br.com.mob1st.processor

import br.com.mob1st.morpheus.annotation.UiState
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import java.io.OutputStream

class MorpheusProcessor(
    private val codeGenerator: CodeGenerator
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbols()
        if (!symbols.iterator().hasNext()) {
            return emptyList()
        }
        val file: OutputStream = codeGenerator.createNewFile(
            dependencies = Dependencies(false),
            packageName = "xuxu.com",
            fileName = "Xuxuzera"
        )
        symbols.forEach {
            it.accept(Visitor(file), Unit)
        }
        file += "package xuxu.com"
        file.close()
        return symbols.filterNot { it.validate() }.toList()
    }
}

private fun Resolver.getSymbols() =
    this.getSymbolsWithAnnotation(UiState::class.qualifiedName.orEmpty())
        .filterIsInstance<KSClassDeclaration>()

private operator fun OutputStream.plusAssign(str: String) {
    this.write(str.toByteArray())
}
