package br.com.mob1st.processor.utils

import java.net.URLClassLoader

internal fun URLClassLoader.createInstance(className: String): Any {
    val cls = loadClass(className)
    val constructor = cls.declaredConstructors.first { it.parameterCount == 0 }
    return constructor.newInstance()
}
