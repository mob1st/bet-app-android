package br.com.mob1st.tests.featuresutils

/**
 * A list that fails when iterating over a specific index.
 * @param indexToFail The index to fail.
 * @param delegate The delegate list.
 */
class FailList<T>(
    indexToFail: Int,
    private val delegate: List<T>,
) : List<T> by delegate {
    private val failIterator = FailIterator(indexToFail, delegate.iterator())

    override fun iterator(): Iterator<T> {
        return failIterator
    }

    @Suppress("IteratorNotThrowingNoSuchElementException")
    private class FailIterator<T>(
        private val indexToFail: Int,
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
