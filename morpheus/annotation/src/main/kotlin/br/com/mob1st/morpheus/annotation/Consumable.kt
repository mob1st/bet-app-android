package br.com.mob1st.morpheus.annotation

/**
 * Indicates which property that produces the side-effect should be consumed
 * @param key the identifier of the property that produces the side-effect
 * @param effect the produced by the side-effect
 */
data class Consumable<K, T>(
    val key: K,
    val effect: T,
)
