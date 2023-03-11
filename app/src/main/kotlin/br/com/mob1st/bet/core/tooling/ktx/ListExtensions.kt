package br.com.mob1st.bet.core.tooling.ktx

/**
 * Calls the given [ifBlock] if there are itens in the list
 * The [getIndex] parameters determine witch value will be sent to the [ifBlock]
 */
inline fun <T> List<T>.ifNotEmpty(getIndex: Int = 0, ifBlock: (T) -> Unit) {
    if (isNotEmpty()) {
        ifBlock(get(getIndex))
    }
}

fun <T> MutableList<T>.putIf(element: T, predicate: (T) -> Boolean) {
    val index = indexOfFirst(predicate)
    if (index >= 0) {
        set(index, element)
    } else {
        add(element)
    }
}
