package br.com.mob1st.bet.core.utils.objects


import kotlinx.serialization.Serializable

/**
 * A tree node that indicates the [current] value in the tree and the next possible [paths]
 */
@Serializable
data class Node<T>(
    val current: T,
    val paths: List<Node<T>>,
)
