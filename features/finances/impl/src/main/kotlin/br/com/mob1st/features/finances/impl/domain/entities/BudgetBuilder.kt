package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Identifiable
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType

/**
 * Represents the state of the category builder in a specific [id].
 * It contains the [manuallyAdded] categories and the [suggestions] available for the user to add.
 * @property id The current step of the category builder.
 * @property manuallyAdded The categories that were manually added by the user.
 * @property suggestions The suggestions available for the user to add.
 */
data class BudgetBuilder(
    override val id: BuilderNextAction.Step,
    val manuallyAdded: List<Category>,
    val suggestions: List<CategorySuggestion>,
) : Identifiable<BuilderNextAction.Step> {
    /**
     * Moves the user to the next step in the category builder.
     * It can be the next step or a completion action, which indicates
     * that the user can proceed to the main area of the app.
     * @return The next action that the user can take.
     * @throws NotEnoughInputsException If there are not enough inputs to proceed to the next step.
     * @see BuilderNextAction.Step.minimumRequiredToProceed
     * @see BuilderNextAction.Step.next
     */
    fun next(): BuilderNextAction {
        val currentInputs = countAddedItems()
        return if (currentInputs >= id.minimumRequiredToProceed) {
            id.next
        } else {
            throw NotEnoughInputsException(id.minimumRequiredToProceed - currentInputs)
        }
    }

    private fun countAddedItems(): Int {
        return manuallyAdded.count { it.amount.cents > 0 } +
            suggestions.count { (it.linkedCategory?.amount?.cents ?: 0) > 0 }
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
         * Whether the step is related to expenses or incomes.
         */
        val isExpense: Boolean

        /**
         * The minimum number of inputs required to proceed to the next step.
         */
        val minimumRequiredToProceed: Int

        /**
         * The type of the category that will be added in this step.
         */
        val type: CategoryType

        /**
         * The next action in the category builder.
         */
        val next: BuilderNextAction
    }
}

/**
 * The first step in the category builder.
 * It is used to add fixed expenses.
 */
data object FixedExpensesStep : BuilderNextAction.Step {
    private const val REQUIRED_INPUTS = 3
    override val isExpense: Boolean = true
    override val minimumRequiredToProceed: Int = REQUIRED_INPUTS
    override val type: CategoryType = CategoryType.Fixed
    override val next: BuilderNextAction = VariableExpensesStep
}

/**
 * The second step in the category builder.
 * It is used to add variable expenses.
 */
data object VariableExpensesStep : BuilderNextAction.Step {
    private const val REQUIRED_INPUTS = 3
    override val isExpense: Boolean = true
    override val minimumRequiredToProceed: Int = REQUIRED_INPUTS
    override val type: CategoryType = CategoryType.Variable
    override val next: BuilderNextAction = FixedIncomesStep
}

/**
 * The third and last step in the category builder.
 * It is used to add fixed incomes.
 */
data object FixedIncomesStep : BuilderNextAction.Step {
    override val isExpense: Boolean = false
    override val minimumRequiredToProceed: Int = 1
    override val type: CategoryType = CategoryType.Fixed
    override val next: BuilderNextAction = BuilderNextAction.Complete
}

/**
 * Represents an error that occurs when the user tries to move to the next step in the category builder but there are
 * not enough inputs yet.
 * @property remainingInputs The number of missing inputs to proceed to the next step.
 */
class NotEnoughInputsException(val remainingInputs: Int) : Exception("Not enough inputs to proceed to the next step")
