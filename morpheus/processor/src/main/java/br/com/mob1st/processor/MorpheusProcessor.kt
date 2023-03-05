package br.com.mob1st.processor

import br.com.mob1st.morpheus.annotation.Morpheus
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.validate
import java.io.OutputStream

class MorpheusProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.info("starting process for Morpheus code generation")
        val symbols = resolver.getSymbols()
        if (!symbols.iterator().hasNext()) {
            logger.info("no data classes with Morpheus annotation found")
            return emptyList()
        }

        symbols.forEach { classDeclaration ->
            val packageName = classDeclaration.packageName.getQualifier()

            val file: OutputStream = codeGenerator.createNewFile(
                dependencies = Dependencies(true),
                packageName = packageName,
                fileName = "Morpheus${classDeclaration.simpleName.getShortName()}"
            )
            classDeclaration.accept(Visitor(), Unit)
            file += "package $packageName"
            file.close()
        }

        return symbols.filterNot { it.validate() }.toList()
    }
}

private fun Resolver.getSymbols() =
    this.getSymbolsWithAnnotation(Morpheus::class.qualifiedName.orEmpty())
        .filterIsInstance<KSClassDeclaration>()
        .filter { kClass ->
            kClass.modifiers.contains(Modifier.DATA)
        }

private operator fun OutputStream.plusAssign(str: String) {
    this.write(str.toByteArray())
}
