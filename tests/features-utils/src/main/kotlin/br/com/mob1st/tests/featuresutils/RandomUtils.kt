package br.com.mob1st.tests.featuresutils

/**
 * Randomly updates an item in the list.
 * @param block The block to update the item.
 * @return The updated list.
 */
fun <T> List<T>.randomUpdate(block: (T) -> T): List<T> {
    val mutable = toMutableList()
    val index = mutable.indices.random()
    val pickedItem = mutable[index]
    mutable[index] = block(pickedItem)
    return mutable
}
