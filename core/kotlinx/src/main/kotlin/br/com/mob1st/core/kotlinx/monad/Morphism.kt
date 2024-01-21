package br.com.mob1st.core.kotlinx.monad

/**
 * Morphism is a function that maps elements of one set to another set.
 * @param From the original set.
 * @param To the target set.
 */
fun interface Morphism<From, To> {

    /**
     * Maps an element of the left set to an element of the right set.
     * @param from the element to be mapped.
     * @return the mapped element.
     */
    operator fun get(from: From): To
}
