package br.com.mob1st.features.finances.publicapi.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money

/**
 * A movement of money from the user's balance.
 * It can be a transaction that already happened or something that will happen in the future
 */
interface BudgetItem {

    /**
     * Textual description of the value.
     */
    val description: String?

    /**
     * The amount of money.
     */
    val amount: Money

    /**
     * The direction of the transaction
     */
    val type: Type

    /**
     * Indicates if the transaction reduces or increases the user's balance
     */
    enum class Type {
        /**
         * Indicates that the transaction reduces the user's balance
         */
        EXPENSE,

        /**
         * Indicates that the transaction increases the user's balance
         */
        INCOME,

        ;
    }
}
