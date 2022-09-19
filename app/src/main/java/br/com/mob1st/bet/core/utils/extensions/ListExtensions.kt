package br.com.mob1st.bet.core.utils.extensions

/**
 * Calls the given [ifBlock] if there are itens in the list
 * The [getIndex] parameters determine witch value will be sent to the [ifBlock]
 */
inline fun <T> List<T>.ifNotEmpty(getIndex: Int = 0, ifBlock: (T) -> Unit) {
    if (isNotEmpty()) {
        ifBlock(get(getIndex))
    }
}