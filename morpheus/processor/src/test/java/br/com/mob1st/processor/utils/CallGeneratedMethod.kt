package br.com.mob1st.processor.utils

inline fun <reified T> newInstance(className: String): T {
    val cls = Class.forName(className)
    val constructor = cls.declaredConstructors.first { it.parameterCount == 0 }
    return constructor.newInstance() as T
}
