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
            property.simpleName.getShortName().replaceFirstChar { char ->
                if (char.isLowerCase()) {
                    char.titlecase(Locale.getDefault())
                } else {
                    char.toString()
                }
            }
        }
    }
}

fun FileSpec.Companion.file(
    classDeclaration: KSClassDeclaration,
    block: FileSpec.Builder.() -> TypeSpec
): FileSpec = builder(
    packageName = classDeclaration.packageName.getQualifier(),
    fileName = "Morpheus${classDeclaration.simpleName.getShortName()}Key"
).apply {
    addType(block())
}.build()

fun FileSpec.Builder.enumsFor(
    properties: Sequence<KSPropertyDeclaration>,
    block: (KSPropertyDeclaration) -> String
) = TypeSpec.enumBuilder(name)
    .apply {
        properties.forEach { property ->
            val constant = block(property)
            addEnumConstant(constant)
        }
    }
    .build()

@OptIn(KspExperimental::class)
inline fun <reified T : Annotation> KSClassDeclaration.getAnnotatedProperties() =
    getDeclaredProperties().filter {
        it.isAnnotationPresent(T::class)
    }
