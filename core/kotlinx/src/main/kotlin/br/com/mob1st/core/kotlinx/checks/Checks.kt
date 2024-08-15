package br.com.mob1st.core.kotlinx.checks

/**
 * Checks if the value is not null and is of type [T].
 * @param T the type to be checked
 * @param value the value to be checked
 */
inline fun <reified T> checkIs(value: Any?): T {
    check(value is T) {
        "Expected non-null value of type ${T::class.java.name} but it was ${value?.javaClass?.name}"
    }
    return value
}

/**
 * Invokes the given [block] if the this receiver is a type of [T]
 * @param block The block to be invoked
 */
inline fun <reified T> Any.ifIs(block: (T) -> Unit) {
    if (this is T) {
        block(this)
    }
}
