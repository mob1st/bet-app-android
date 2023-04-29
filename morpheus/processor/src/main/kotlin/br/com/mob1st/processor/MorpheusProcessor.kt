package br.com.mob1st.processor

import br.com.mob1st.processor.writers.AnnotationFilter
import br.com.mob1st.processor.writers.Writer
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

/**
 * This class is responsible for processing the data classes annotated with @Morpheus.
 */
class MorpheusProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.info("Morpheus processor started")
        val filter = AnnotationFilter(resolver, logger)
        val symbols = filter()
        if (symbols.iterator().hasNext()) {
            val writer = Writer(codeGenerator, logger)
            symbols.forEach { classDeclaration ->
                writer(classDeclaration)
            }
        } else {
            logger.info("no @Morpheus found")
        }

        return emptyList()
    }
}
