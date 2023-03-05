package br.com.mob1st.morpheus.annotation.utils

internal fun <T> List<T>.poll(): List<T> = mutate {
    removeAt(0)
}

internal fun <T> List<T>.pop(): List<T> = mutate {
    removeAt(lastIndex)
}

internal fun <T> List<T>.removeFirst(predicate: (T) -> Boolean): List<T> = mutate {
    val index = indexOfFirst(predicate)
    removeAt(index)
}

private fun <T> List<T>.mutate(block: MutableList<T>.() -> Unit): List<T> {
    return toMutableList().apply(block).toList()
}
