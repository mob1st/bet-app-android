package br.com.mob1st.processor.writers

import br.com.mob1st.processor.utils.upperCaseFirstChar
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.TypeSpec

internal class EnumCreator(
    private val logger: KSPLogger,
    enumName: String,
) {

    private val enumBuilder = TypeSpec.enumBuilder(enumName)

    init {
        logger.info("creating enum $enumName")
    }

    fun addConstant(
        propertyDeclaration: KSPropertyDeclaration,
    ): String {
        val propName = propertyDeclaration.simpleName.asString()
        logger.info("creating enum constant for property $propName")
        val constant = propName.upperCaseFirstChar()
        enumBuilder.addEnumConstant(constant)
        return constant
    }

    fun build() = enumBuilder.build()
}
