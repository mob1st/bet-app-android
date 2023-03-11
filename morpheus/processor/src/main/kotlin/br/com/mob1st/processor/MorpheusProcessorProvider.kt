package br.com.mob1st.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/***
 * This class is responsible for creating the [MorpheusProcessor] instance.
 */
class MorpheusProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return MorpheusProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger
        )
    }
}
