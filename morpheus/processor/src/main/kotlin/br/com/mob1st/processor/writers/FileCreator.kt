package br.com.mob1st.processor.writers

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec

internal class FileCreator(
    logger: KSPLogger,
    classDeclaration: KSClassDeclaration,
) {

    val packageName = classDeclaration.packageName.asString()
    val fileName = "${classDeclaration.simpleName.getShortName()}EffectKey"

    private val fileSpecBuilder = FileSpec.builder(packageName, fileName)

    init {
        logger.info("creating file for class ${classDeclaration.simpleName.asString()}")
    }

    fun addFunction(funSpec: FunSpec) = apply {
        fileSpecBuilder.addFunction(funSpec)
    }

    fun addEnum(enumSpec: TypeSpec) = apply {
        fileSpecBuilder.addType(enumSpec)
    }

    fun build() = fileSpecBuilder.build()
}
