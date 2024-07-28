package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Identifiable
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrenceType

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

/**
 * Represents the next action that the user can take in the category builder.
 */
sealed interface BuilderNextAction {
    /**
     * Action that completes the category builder and allows the user to proceed to the main area of the app.
     */
    data object Complete : BuilderNextAction

    /**
     * A specific step in the category builder.
     */
    sealed interface Step : BuilderNextAction {
        /**
         * The minimum number of inputs required to proceed to the next step.
         */
        val minimumRequiredToProceed: Int

        /**
         * The next action in the category builder.
         */
        val next: BuilderNextAction

        /**
         * Whether the categories in this step are expenses or incomes.
         */
        val isExpense: Boolean

        /**
         * The default recurrence for the categories in this step.
         */
        val type: RecurrenceType
    }
}

/**
 * The first step in the category builder.
 * It is used to add fixed expenses.
 */
data object FixedExpensesStep : BuilderNextAction.Step {
    private const val REQUIRED_INPUTS = 3
    override val minimumRequiredToProceed: Int = REQUIRED_INPUTS
    override val next: BuilderNextAction = VariableExpensesStep
    override val type: RecurrenceType = RecurrenceType.Fixed
    override val isExpense: Boolean = true
}

/**
 * The second step in the category builder.
 * It is used to add variable expenses.
 */
data object VariableExpensesStep : BuilderNextAction.Step {
    private const val REQUIRED_INPUTS = 3
    override val minimumRequiredToProceed: Int = REQUIRED_INPUTS
    override val next: BuilderNextAction = FixedIncomesStep
    override val type: RecurrenceType = RecurrenceType.Variable
    override val isExpense: Boolean = true
}

/**
 * The third step in the category builder.
 * It is used to add seasonal expenses.
 */
data object SeasonalExpensesStep : BuilderNextAction.Step {
    override val minimumRequiredToProceed: Int = 0
    override val next: BuilderNextAction = FixedIncomesStep
    override val type: RecurrenceType = RecurrenceType.Seasonal
    override val isExpense: Boolean = true
}

/**
 * The fourth step in the category builder.
 * It is used to add fixed incomes.
 */
data object FixedIncomesStep : BuilderNextAction.Step {
    override val minimumRequiredToProceed: Int = 1
    override val next: BuilderNextAction = BuilderNextAction.Complete
    override val type: RecurrenceType = RecurrenceType.Fixed
    override val isExpense: Boolean = false
}
