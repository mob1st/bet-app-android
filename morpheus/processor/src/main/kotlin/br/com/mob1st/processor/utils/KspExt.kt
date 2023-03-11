package br.com.mob1st.processor.utils

import br.com.mob1st.morpheus.annotation.ConsumableEffect
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

@OptIn(KspExperimental::class)
fun KSClassDeclaration.getConsumableProperties(): Sequence<KSPropertyDeclaration> = getDeclaredProperties()
    .filter { it.isAnnotationPresent(ConsumableEffect::class) }
