package br.com.mob1st.processor.writers

import br.com.mob1st.morpheus.annotation.Consumable
import br.com.mob1st.processor.utils.upperCaseFirstChar
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toTypeName

internal class ConsumeExtensionCreator(
    private val logger: KSPLogger,
    private val classDeclaration: KSClassDeclaration,
    private val enumName: String,
) {

    operator fun invoke(
        propertyDeclaration: KSPropertyDeclaration,
        enumConstant: String,
    ): FunSpec {
        val propName = propertyDeclaration.simpleName.asString()
        logger.info("creating consume extension for property $propName")
        val extensionName = "consume${propName.upperCaseFirstChar()}"
        val classType = classDeclaration.asType(emptyList()).toTypeName()
        val returnType = Consumable::class.asClassName().parameterizedBy(
            ClassName(
                classDeclaration.packageName.asString(),
                enumName
            ),
            propertyDeclaration.type.toTypeName()
        )

        return FunSpec.builder(extensionName)
            .receiver(classType)
            .returns(returnType)
            .addStatement(
                "return %T(key = %N.%N, effect = %N,)",
                Consumable::class,
                enumName,
                enumConstant,
                propName
            )
            .build()
    }
}
