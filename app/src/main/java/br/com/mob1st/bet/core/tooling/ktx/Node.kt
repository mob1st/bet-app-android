package br.com.mob1st.bet.core.tooling.ktx

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

/**
 * A tree node that indicates the [current] value in the tree and the next possible [paths]
 */
@Serializable
@Keep
data class Node<out T>(
    val current: T,
    val paths: List<Node<T>>
)
