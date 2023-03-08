package br.com.mob1st.processor.writters

import br.com.mob1st.morpheus.annotation.Consumable
import br.com.mob1st.morpheus.annotation.ConsumableEffect
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import java.util.Locale

fun morpheus(classDeclaration: KSClassDeclaration): FileSpec {
    return FileSpec.file(classDeclaration) { ->
        classDeclaration
            .getAnnotatedProperties<ConsumableEffect>()
    }
}

fun FileSpec.Companion.file(
    classDeclaration: KSClassDeclaration,
    block: FileSpec.Builder.() -> Sequence<KSPropertyDeclaration>
): FileSpec = builder(
    packageName = classDeclaration.packageName.asString(),
    fileName = "${classDeclaration.simpleName.getShortName()}EffectKey"
).apply {
    var enumBuilder = TypeSpec.enumBuilder(name)
    val properties = block()
    properties.forEach {
        enumBuilder = enumBuilder.addEnumConstant(it.asEnumConstant())
    }
    val enum = enumBuilder.build()
    addType(enum)
    properties.forEach { property ->
        val ext = property.asExtension(
            classDeclaration = classDeclaration,
            packageName = packageName,
            enumTypeSpec = enum,
            enumConstant = property.asEnumConstant()
        )
        addFunction(ext)
    }
    addFunction(classDeclaration.clearExtension(packageName, enum, enum.enumConstants.keys))
}.build()

fun KSPropertyDeclaration.asEnumConstant(): String {
    return simpleName.getShortName().replaceFirstChar { char ->
        if (char.isLowerCase()) {
            char.titlecase(Locale.getDefault())
        } else {
            char.toString()
        }
    }
}

fun KSPropertyDeclaration.asExtension(
    classDeclaration: KSClassDeclaration,
    packageName: String,
    enumTypeSpec: TypeSpec,
    enumConstant: String
): FunSpec {
    val classType = classDeclaration.asType(emptyList()).toTypeName()
    val returnType = Consumable::class.asClassName().parameterizedBy(
        ClassName(
            packageName,
            enumTypeSpec.name.orEmpty()
        ),
        type.toTypeName()
    )
    return FunSpec.builder("consume$enumConstant")
        .receiver(classType)
        .returns(returnType)
        .addStatement(
            "return %T(key = %N.%N, effect = %N)",
            Consumable::class,
            enumTypeSpec,
            enumConstant,
            simpleName.getShortName()
        )
        .build()
}

fun KSClassDeclaration.clearExtension(
    packageName: String,
    enumTypeSpec: TypeSpec,
    constants: Collection<String>
): FunSpec {
    val classType = asType(emptyList()).toTypeName()
    val parameterType = Consumable::class.asClassName().parameterizedBy(
        ClassName(
            packageName,
            enumTypeSpec.name.orEmpty()
        ),
        STAR
    )
    //    val stackStrategy = StackStrategy<String>()::class.createInstance()
    //    val kType = ConsumptionStrategy::class.createType(
    //        listOf(
    //
    //        )
    //    )

    val preWhen = FunSpec.builder("clear")
        .receiver(classType)
        .returns(classType)
        .addParameter(
            "consumable",
            parameterType
        )
    var whenBlock = preWhen.beginControlFlow("return when (consumable.key)")
    constants.forEach { constant ->
        whenBlock = whenBlock.addStatement(
            "%N.%N -> copy(%N = null)",
            enumTypeSpec,
            constant,
            constant.replaceFirstChar { it.lowercaseChar() }
        )
    }
    return whenBlock.endControlFlow()
        .build()
}

@OptIn(KspExperimental::class)
inline fun <reified T : Annotation> KSClassDeclaration.getAnnotatedProperties() =
    getDeclaredProperties().filter {
        it.isAnnotationPresent(T::class)
    }
