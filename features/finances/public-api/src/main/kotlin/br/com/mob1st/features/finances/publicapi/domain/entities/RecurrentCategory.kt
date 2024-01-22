package br.com.mob1st.features.finances.publicapi.domain.entities

import br.com.mob1st.core.kotlinx.structures.Id
import br.com.mob1st.core.kotlinx.structures.Identifiable
import kotlinx.datetime.Month

/**
 * A type of transaction that happens recurrently, every month, week or year.
 */
sealed interface RecurrentCategory : Identifiable, BudgetItem {

    /**
     * The movement of money that happens recurrently
     */
    val recurrentTransaction: RecurrentTransaction

    /**
     * A fixed recurrent transaction.
     * It happens every month, on the same day. Eg: rent, internet, etc.
     * @property dayOfMonth The day of the month that the transaction happens
     */
    data class Fixed(
        override val recurrentTransaction: RecurrentTransaction,
        override val id: Id = Id(),
        val dayOfMonth: Int = DEFAULT_DAY_OF_MONTH,
    ) : RecurrentCategory, BudgetItem by recurrentTransaction

    /**
     * A variable recurrent transaction.
     * It happens every week and doesn't have a fixed day or even a fixed amount. Eg: lunch, bars, etc.
     */
    data class Variable(
        override val recurrentTransaction: RecurrentTransaction,
        override val id: Id = Id(),
    ) : RecurrentCategory, BudgetItem by recurrentTransaction
    data class Seasonal(
        override val recurrentTransaction: RecurrentTransaction,
        val month: Month,
        val day: Int = DEFAULT_DAY_OF_MONTH,
        override val id: Id = Id(),
    ) : RecurrentCategory, BudgetItem by recurrentTransaction

    companion object {
        const val DEFAULT_DAY_OF_MONTH = 5
    }
}
