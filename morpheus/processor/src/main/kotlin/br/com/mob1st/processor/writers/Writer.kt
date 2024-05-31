package br.com.mob1st.processor.writers

import br.com.mob1st.processor.utils.getConsumableProperties
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.writeTo

internal class Writer(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) {
    @OptIn(KspExperimental::class)
    operator fun invoke(classDeclaration: KSClassDeclaration) {
        val fileCreator = FileCreator(logger, classDeclaration)
        val enumCreator = EnumCreator(logger, fileCreator.fileName)
        val consumeExtensionCreator =
            ConsumeExtensionCreator(
                logger = logger,
                classDeclaration = classDeclaration,
                enumName = fileCreator.fileName,
            )
        val clearEffectExtensionCreator =
            ClearEffectExtensionCreator(
                logger = logger,
                classDeclaration = classDeclaration,
                packageName = fileCreator.packageName,
                enumName = fileCreator.fileName,
            )
        classDeclaration.getConsumableProperties().forEach {
            val constantName = enumCreator.addConstant(it)
            fileCreator.addFunction(
                consumeExtensionCreator.invoke(it, constantName),
            )
            clearEffectExtensionCreator.addStatement(constantName)
        }

        val file =
            fileCreator.addFunction(clearEffectExtensionCreator.build())
                .addEnum(enumCreator.build())
                .build()

        logger.info("writing file ${fileCreator.fileName}.kt")
        file.writeTo(codeGenerator, Dependencies(true))
    }
}
