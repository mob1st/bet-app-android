package br.com.mob1st.processor.utils

internal fun String.upperCaseFirstChar(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
