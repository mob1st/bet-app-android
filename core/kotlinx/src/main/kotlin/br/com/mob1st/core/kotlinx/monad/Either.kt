package br.com.mob1st.core.kotlinx.monad

@Suppress("UNCHECKED_CAST")
@JvmInline
value class Either<out L, out R> private constructor(
    private val value: Any,
) {

    val isLeft: Boolean get() = left() != null
    val isRight: Boolean get() = !isLeft

    fun left(): L? = value as? L
    fun right(): R? = value as? R

    companion object {
        fun <L, R> left(value: L) = Either<L, R>(value as Any)
        fun <L, R> right(value: R) = Either<L, R>(value as Any)
    }
}

inline fun <L, R> Either<L, R>.onLeft(block: (L) -> Unit): Either<L, R> {
    if (isLeft) {
        block(checkNotNull(left()))
    }
    return this
}

inline fun <L, R> Either<L, R>.onRight(block: (R) -> Unit): Either<L, R> {
    if (isRight) {
        block(checkNotNull(right()))
    }
    return this
}
