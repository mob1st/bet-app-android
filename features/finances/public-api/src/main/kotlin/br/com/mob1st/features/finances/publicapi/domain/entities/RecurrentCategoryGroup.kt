package br.com.mob1st.features.finances.publicapi.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money

/**
 * Group of recurrent categories
 * @property items list of recurrent categories
 * @param T type of recurrent category
 */
interface RecurrentCategoryGroup<T : RecurrentCategory> {

    /**
     * List of recurrent categories
     */
    val items: List<T>

    /**
     * The total amount of all recurrent categories
     */
    val total: Money

    /**
     * Calculate the proportion of the recurrent category in relation to the [total]
     */
    fun proportionOf(position: Int): Int
}
