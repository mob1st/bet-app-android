package br.com.mob1st.processor

import br.com.mob1st.morpheus.annotation.ConsumableEffect
import br.com.mob1st.morpheus.annotation.Morpheus
import br.com.mob1st.processor.writters.getAnnotatedProperties
import br.com.mob1st.processor.writters.morpheus
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.ksp.writeTo
import java.io.OutputStream

class MorpheusProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.info("starting process for Morpheus code generation")
        val symbols = resolver.getSymbols()
        if (!symbols.iterator().hasNext()) {
            logger.info("no data classes with Morpheus annotation found")
            return emptyList()
        }

        symbols.forEach { classDeclaration ->
            morpheus(classDeclaration)
                .writeTo(codeGenerator, Dependencies(true))
        }

        return emptyList()
    }
}

private fun Resolver.getSymbols() =
    this.getSymbolsWithAnnotation(Morpheus::class.qualifiedName.orEmpty())
        .filterIsInstance<KSClassDeclaration>()
        .onEach { kClass ->
            if (!kClass.modifiers.contains(Modifier.DATA)) {
                error("class ${kClass.simpleName.asString()} with Morpheus annotation must be a data class")
            }
        }
        .onEach { kClass ->
            val annotations = kClass.getAnnotatedProperties<ConsumableEffect>()
            if (!annotations.iterator().hasNext()) {
                error(
                    "no @ConsumableEffect found in data class ${kClass.simpleName.asString()} with Morpheus annotation"
                )
            }
        }

private operator fun OutputStream.plusAssign(str: String) {
    this.write(str.toByteArray())
}
