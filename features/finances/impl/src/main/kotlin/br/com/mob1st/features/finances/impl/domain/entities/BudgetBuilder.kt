package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Identifiable

/**
 * Represents the state of the category builder in a specific [id].
 * It contains the [manuallyAdded] categories and the [suggestions] available for the user to add.
 * @property id The current step of the category builder.
 * @property categories The list of categories to be presented to the user.
 */
data class BudgetBuilder(
    override val id: BuilderNextAction.Step,
    private val categories: List<Category>,
) : Identifiable<BuilderNextAction.Step> {
    /**
     * Nests the [id] property to provide access to the next builder action.
     */
    val next = id.next

    /**
     * The categories that were manually added by the user.
     */
    val manuallyAdded: List<Category>

    /**
     * The suggestions available for the user to add.
     */
    val suggestions: List<Category>

    init {
        val (suggestions, manuallyAdded) = categories.partition { it.isSuggested }
        this.manuallyAdded = manuallyAdded
        this.suggestions = suggestions
    }

    /**
     * Calculates the number of remaining inputs that the user needs to add to proceed to the next step.
     * A positive number indicates that the user needs to add more inputs before proceeding to the next action.
     * @return The number of remaining inputs, or zero if more inputs were added than necessary.
     */
    fun calculateRemainingInputs(): Int {
        val diff = id.minimumRequiredToProceed - countAddedItems()
        return maxOf(diff, 0)
    }

    private fun countAddedItems(): Int {
        return categories.count { it.amount.cents > 0 }
    }

    companion object {
        /**
         * Returns the first step of the category builder.
         */
        fun firstStep(): BuilderNextAction.Step = FixedExpensesStep
    }
}
