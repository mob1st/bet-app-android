package br.com.mob1st.core.data

import kotlinx.coroutines.flow.Flow

/**
 * Supports data access for a single value.
 * It is typically used to data stores or values cached in ram memory.
 * @param T the type of the data.
 */
interface UnitaryDataSource<T> {
    /**
     * The data. Every time the [set] is called a new emission should be triggered.
     */
    val data: Flow<T>

    /**
     * Sets the data.
     * It should trigger a new emission on [data].
     * @param data the data to be set.
     */
    suspend fun set(data: T)
}
