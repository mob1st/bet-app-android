package br.com.mob1st.processor.writers

import br.com.mob1st.morpheus.annotation.ConsumableEffect
import br.com.mob1st.morpheus.annotation.Morpheus
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier

class AnnotationFilter(
    private val resolver: Resolver,
    private val logger: KSPLogger,
) {

    operator fun invoke(): Sequence<KSClassDeclaration> {
        return resolver.getSymbols(logger)
    }
}

private fun Resolver.getSymbols(logger: KSPLogger) = getSymbolsWithAnnotation(Morpheus::class.qualifiedName.orEmpty())
    .filterIsInstance<KSClassDeclaration>()
    .onEach { kClass ->
        if (!kClass.modifiers.contains(Modifier.DATA)) {
            val m = "class ${kClass.simpleName.asString()} with Morpheus annotation must be a data class"
            error(m)
        }
    }
    .onEach { kClass ->
        logger.info("class ${kClass.simpleName.asString()} with Morpheus annotation found")
        val annotations = kClass.getAnnotatedProperties<ConsumableEffect>()
        if (!annotations.iterator().hasNext()) {
            error(
                "no @ConsumableEffect found in data class ${kClass.simpleName.asString()} with Morpheus annotation"
            )
        }
    }
