package br.com.mob1st.core.kotlinx.collections

import kotlinx.collections.immutable.PersistentList

/**
 * Updates the item at the given index with the result of the block.
 * It gets the current item at the index, applies the block to it and sets the new item at the index.
 * @param index The index of the item to update.
 * @param block The block to apply to the item at the index.
 * @return A new [PersistentList] with the updated item.
 */
fun <T> PersistentList<T>.update(
    index: Int,
    block: (T) -> (T),
): PersistentList<T> {
    val currentItem = get(index)
    val newItem = block(currentItem)
    return set(index, newItem)
}
