package br.com.mob1st.tests.featuresutils

/**
 * A list that fails when iterating over a specific index.
 * It can be useful to check if bulk operations on database are using a single transaction.
 * How to make it:
 * - Create a BrokenList with the index to fail and the delegate list of items to be written/deleted.
 * - Execute the bulk operation using the BrokenList as parameter.
 * - Check if there was changes in the database or not.
 * If the operation is not using a single transaction, the database will have some items written/deleted.
 * Otherwise, it remains the same before the operation.
 * @param indexToFail The index to fail.
 * @param delegate The delegate list.
 * @param T The type of the list.
 */
class BrokenList<T>(
    indexToFail: Int,
    private val delegate: List<T>,
) : List<T> by delegate {
    private val brokenIterator = BrokenIterator(indexToFail, delegate.iterator())

    override fun iterator(): Iterator<T> {
        return brokenIterator
    }

    override fun get(index: Int): T {
        if (index == brokenIterator.indexToFail) {
            error("Fail on index $index")
        }
        return delegate[index]
    }

    @Suppress("IteratorNotThrowingNoSuchElementException")
    private class BrokenIterator<T>(
        val indexToFail: Int,
        private val iterator: Iterator<T>,
    ) : Iterator<T> by iterator {
        private var index = 0

        override fun next(): T {
            if (index == indexToFail) {
                error("Fail on index $index")
            } else {
                index++
                return iterator.next()
            }
        }
    }
}

fun <T> List<T>.failOnIndex(indexToFail: Int): List<T> {
    return BrokenList(indexToFail, this)
}
