package br.com.mob1st.core.kotlinx.structures

/**
 * BI-directional map, allowing searching by direct and reverse keys.
 * It's a simple wrapper on top of two maps, one for the left to right and another for the right to left.
 * @param L The type of the left key.
 * @param R The type of the right key.
 * @property leftToRight The map from left to right.
 * @property rightToLeft The map from right to left.
 */
class BiMap<L, R>(
    private val leftToRight: MutableMap<L, R> = mutableMapOf(),
    private val rightToLeft: MutableMap<R, L> = mutableMapOf(),
) {
    /**
     * Gets the right value from the left key.
     * @param left The left key or null if not found.
     */
    fun getLeft(left: L): R? = leftToRight[left]

    /**
     * Gets the right value from the left key.
     * @param right The right key or null if not found.
     * @return The right value or null if not found.
     */
    fun getRight(right: R): L? = rightToLeft[right]

    /**
     * Gets the right value from the left key, throwing an exception if not found.
     * @param left The left key.
     * @return The right value.
     * @throws NoSuchElementException If the left key is not found.
     */
    fun getLeftValue(left: L): R = leftToRight.getValue(left)

    /**
     * Gets the right value from the left key, throwing an exception if not found.
     * @param right The right key.
     * @return The right value.
     * @throws NoSuchElementException If the right key is not found.
     */
    fun getRightValue(right: R): L = rightToLeft.getValue(right)
}

/**
 * Creates a bi-directional map.
 * It uses the giben [pairs] of left and right values and both will be used as keys and values.
 * @param L The type of the left key.
 * @param R The type of the right key.
 * @param pairs The pairs of left and right values.
 * @return The bi-directional map.
 */
fun <L, R> biMapOf(vararg pairs: Pair<L, R>): BiMap<L, R> {
    val leftToRight = mutableMapOf<L, R>()
    val rightToLeft = mutableMapOf<R, L>()
    pairs.forEach { (left, right) ->
        leftToRight[left] = right
        rightToLeft[right] = left
    }
    return BiMap(leftToRight, rightToLeft)
}

fun <L, R> List<Pair<L, R>>.toBiMap(): BiMap<L, R> {
    val array = toTypedArray()
    return biMapOf(*array)
}
