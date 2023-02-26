package br.com.mob1st.bet.core.tooling.vm

sealed class AsyncData {

    object NotTriggeredYet : AsyncData()

    object Loading : AsyncData()

    class Failure(val throwable: Throwable) : AsyncData()

    class Success<T>(val data: T) : AsyncData()

    fun isLoading(): Boolean = this is Loading

    fun <T> data(): T? = if (this is Success<*>) {
        @Suppress("UNCHECKED_CAST")
        data as? T
    } else {
        null
    }

    fun throwable(): Throwable? = if (this is Failure) {
        throwable
    } else {
        null
    }
}

operator fun <U> AsyncData.plus(item: U): Pair<AsyncData, U> = this to item
