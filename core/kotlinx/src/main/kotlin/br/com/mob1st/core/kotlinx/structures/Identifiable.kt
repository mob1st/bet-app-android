package br.com.mob1st.core.kotlinx.structures

import java.util.UUID

/**
 * Something that can be identified by an [Id].
 */
interface Identifiable {
    /**
     * The unique identifier.
     */
    val id: Id
}

/**
 * A unique identifier.
 */
@JvmInline
value class Id(private val value: String = generateId()) : Comparable<Id> {
    override fun compareTo(other: Id): Int {
        return value.compareTo(other.value)
    }
}

/**
 * Generates a random id.
 */
fun generateId() = UUID.randomUUID().toString()
