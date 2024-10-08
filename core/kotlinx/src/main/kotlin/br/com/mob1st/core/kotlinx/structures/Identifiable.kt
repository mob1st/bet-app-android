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
interface RowId : Comparable<RowId>, Serializable {
    /**
     * The unique identifier.
     */
    val value: Long

    /**
     * Checks if the identifier is valid.
     * Some entities can be created at runtime and they don't have a valid identifier.
     * In this case, usually the default value is zero.
     */
    fun isWritten() = value > 0

    override fun compareTo(other: RowId): Int {
        return value.compareTo(other.value)
    }
}

/**
 * Generates a random id.
 */
fun generateStringId() = UUID.randomUUID().toString()
