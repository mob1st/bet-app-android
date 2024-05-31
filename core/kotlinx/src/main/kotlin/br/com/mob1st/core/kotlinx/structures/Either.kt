package br.com.mob1st.core.kotlinx.structures

/**
 * Abstracts the concept of a value that can be either a [Left] or a [Right].
 * It's useful for sealed classes/interfaces that have two possible states.
 * @param Left the type of the left value.
 * @param Right the type of the right value.
 */

interface Either<Left, Right>

/**
 * Access the left value or null if it's a Right.
 */
inline fun <reified Left> Either<Left, *>.leftOrNull(): Left? {
    return this as? Left
}

/**
 * Access the right value or null if it's a Left].
 */
inline fun <reified Right> Either<*, Right>.rightOrNull(): Right? {
    return this as? Right
}

/**
 * Represents the left value of an [Either] or throws an [IllegalStateException] if it's a Right.
 */
inline fun <reified Left> Either<Left, *>.left(): Left {
    check(this is Left) {
        "Expected to be a ${Left::class.java.simpleName} but it was a ${this::class.java.simpleName}"
    }
    return this
}

/**
 * Represents the right value of an [Either] or throws an [IllegalStateException] if it's a Left.
 */
inline fun <reified Right> Either<*, Right>.right(): Right {
    check(this is Right) {
        "Expected to be a ${Right::class.java.simpleName} but it was a ${this::class.java.simpleName}"
    }
    return this
}

/**
 * Fold the [Either] into a single value.
 * @param onLeft the function to be called if it's a [Left].
 * @param onRight the function to be called if it's a [Right].
 * @param T the type of the returned value.
 * @param Left the type of the left value.
 * @param Right the type of the right value.
 * @return the value returned by the [onLeft] or [onRight] function.
 */
inline fun <reified Left, reified Right, T> Either<Left, Right>.fold(
    onLeft: (Left) -> T,
    onRight: (Right) -> T,
): T {
    check(this is Left || this is Right) {
        "Expected to be a ${Left::class.java.simpleName} or a ${Right::class.java.simpleName} but it was a " +
            this::class.java.simpleName
    }
    return when (this) {
        is Left -> onLeft(this)
        else -> onRight(this as Right)
    }
}

/**
 * Execute the [onLeft] function if it's a [Left].
 */
inline fun <reified Left, Right> Either<Left, Right>.onLeft(onLeft: (Left) -> Unit): Either<Left, Right> {
    if (this is Left) {
        onLeft(this)
    }
    return this
}

/**
 * Execute the [onRight] function if it's a [Right].
 */
inline fun <Left, reified Right> Either<Left, Right>.onRight(onRight: (Right) -> Unit): Either<Left, Right> {
    if (this is Right) {
        onRight(this)
    }
    return this
}
