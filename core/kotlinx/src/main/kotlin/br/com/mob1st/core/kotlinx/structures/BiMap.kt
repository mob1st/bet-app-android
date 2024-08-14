package br.com.mob1st.core.kotlinx.structures

class BiMap<L, R>(
    private val leftToRight: MutableMap<L, R> = mutableMapOf(),
    private val rightToLeft: MutableMap<R, L> = mutableMapOf(),
) {
    fun getLeft(left: L): R? = leftToRight[left]

    fun getRight(right: R): L? = rightToLeft[right]

    fun getLeftValue(left: L): R = leftToRight.getValue(left)

    fun getRightValue(right: R): L = rightToLeft.getValue(right)
}

fun <L, R> biMapOf(vararg pairs: Pair<L, R>): BiMap<L, R> {
    val leftToRight = mutableMapOf<L, R>()
    val rightToLeft = mutableMapOf<R, L>()
    pairs.forEach { (left, right) ->
        leftToRight[left] = right
        rightToLeft[right] = left
    }
    return BiMap(leftToRight, rightToLeft)
}
