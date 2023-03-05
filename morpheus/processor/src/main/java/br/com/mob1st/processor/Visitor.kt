package br.com.mob1st.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid

@OptIn(KspExperimental::class)
class Visitor : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
    }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
    }
}
