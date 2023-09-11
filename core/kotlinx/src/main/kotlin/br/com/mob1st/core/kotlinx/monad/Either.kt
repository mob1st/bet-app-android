package br.com.mob1st.core.kotlinx.monad

import java.util.Queue

@Suppress("UNCHECKED_CAST")
@JvmInline
value class Either<out L, out R> private constructor(
    private val value: Any,
) {

    val isLeft: Boolean get() = left != null
    val isRight: Boolean get() = !isLeft

    val left: L? get() = value as? L
    val right: R? get() = value as? R

    override fun toString(): String {
        return if (left != null) {
            "Left($left)"
        } else {
            "Right($right)"
        }
    }

    companion object {
        fun <L, R> left(value: L) = Either<L, R>(value as Any)
        fun <L, R> right(value: R) = Either<L, R>(value as Any)
    }
}

inline fun <L, R> Either<L, R>.onLeft(block: (L) -> Unit): Either<L, R> {
    if (isLeft) {
        block(checkNotNull(left))
    }
    return this
}

inline fun <L, R> Either<L, R>.onRight(block: (R) -> Unit): Either<L, R> {
    if (isRight) {
        block(checkNotNull(right))
    }
    return this
}

inline fun <reified L, reified R> Queue<Either<L, R>>.offer(value: L) = offer(Either.left(value))

inline fun <reified L, reified R> Queue<Either<L, R>>.offer(value: R) = offer(Either.right(value))
