package br.com.mob1st.processor.writers

import br.com.mob1st.morpheus.annotation.Consumable
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toTypeName

internal class ClearEffectExtensionCreator(
    private val logger: KSPLogger,
    classDeclaration: KSClassDeclaration,
    packageName: String,
    private val enumName: String,
) {

    private val builder: FunSpec.Builder
    private val classType = classDeclaration.asType(emptyList()).toTypeName()
    private val parameterType = Consumable::class.asClassName().parameterizedBy(
        ClassName(packageName, enumName),
        STAR
    )

    init {
        logger.info("creating clearEffect extension creator")
        builder = FunSpec.builder("clearEffect")
            .receiver(classType)
            .returns(classType)
            .addParameter(
                "consumable",
                parameterType
            )
            .beginControlFlow("return when (consumable.key)")
    }

    fun addStatement(constant: String) {
        logger.info("adding $constant conditional into clearEffect")
        builder.addStatement(
            "%N.%N -> copy(%N = null)",
            enumName,
            constant,
            constant.replaceFirstChar { it.lowercaseChar() }
        )
    }

    fun build(): FunSpec {
        return builder
            .endControlFlow()
            .build()
    }
}
