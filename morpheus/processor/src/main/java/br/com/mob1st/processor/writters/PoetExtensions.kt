package br.com.mob1st.processor.writters

import br.com.mob1st.morpheus.annotation.ConsumableEffect
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import java.util.Locale

fun morpheus(classDeclaration: KSClassDeclaration): FileSpec {
    return FileSpec.file(classDeclaration) { ->
        enumsFor(classDeclaration.getAnnotatedProperties<ConsumableEffect>()) { property ->
            constant(property)
        }
    }
}

fun FileSpec.Companion.file(
    classDeclaration: KSClassDeclaration,
    block: FileSpec.Builder.() -> Unit
): FileSpec = builder(
    packageName = classDeclaration.packageName.getQualifier(),
    fileName = "Morpheus${classDeclaration.simpleName.getShortName()}Key"
).apply(block).build()

fun FileSpec.Builder.enumsFor(
    properties: Sequence<KSPropertyDeclaration>,
    block: TypeSpec.Builder.(KSPropertyDeclaration) -> Unit
) {
    val type = TypeSpec.enumBuilder(name)
        .apply {
            properties.forEach {
                block(it)
            }
        }.build()
    addType(type)
}

fun TypeSpec.Builder.constant(property: KSPropertyDeclaration) {
    val name = property.simpleName.getShortName().replaceFirstChar { char ->
        if (char.isLowerCase()) {
            char.titlecase(Locale.getDefault())
        } else {
            char.toString()
        }
    }
    addEnumConstant(name)
}

@OptIn(KspExperimental::class)
inline fun <reified T : Annotation> KSClassDeclaration.getAnnotatedProperties() =
    getDeclaredProperties().filter {
        it.isAnnotationPresent(T::class)
    }
