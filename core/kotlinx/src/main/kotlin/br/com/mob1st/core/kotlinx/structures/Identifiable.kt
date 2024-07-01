package br.com.mob1st.core.kotlinx.structures

import java.io.Serializable
import java.util.UUID

/**
 * Something that can be identified by an [Uuid].
 */
interface Identifiable<T> {
    /**
     * The unique identifier.
     */
    val id: T
}

/**
 * A unique identifier in text format.
 * It's usually a UUID style string that can be used to identify an entity.
 */
@JvmInline
value class Uuid(private val value: String = generateStringId()) : Comparable<Uuid>, Serializable by value {
    override fun compareTo(other: Uuid): Int {
        return value.compareTo(other.value)
    }
}

/**
 * A unique identifier in long format.
 * It can be an auto-incremented number or a random number.
 */
@JvmInline
value class RowId(val value: Long = 0) : Comparable<RowId>, Serializable by value {
    override fun compareTo(other: RowId): Int {
        return value.compareTo(other.value)
    }
}

/**
 * Generates a random id.
 */
fun generateStringId() = UUID.randomUUID().toString()
