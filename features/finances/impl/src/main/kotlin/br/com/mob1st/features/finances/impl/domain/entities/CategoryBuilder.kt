package br.com.mob1st.features.finances.impl.domain.entities

import arrow.core.raise.either
import br.com.mob1st.core.kotlinx.structures.Identifiable
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType

/**
 * Represents the state of the category builder in a specific [id].
 * It contains the [manuallyAdded] categories and the [suggestions] available for the user to add.
 * @property id The current step of the category builder.
 * @property manuallyAdded The categories that were manually added by the user.
 * @property suggestions The suggestions available for the user to add.
 */
data class CategoryBuilder(
    override val id: BuilderNextAction.Step,
    val manuallyAdded: List<Category>,
    val suggestions: List<CategorySuggestion>,
) : Identifiable<BuilderNextAction.Step> {
    /**
     * Moves to the next step in the category builder if there are enough inputs added in [manuallyAdded] or
     * [suggestions].
     * @return The next step in the category builder or [NotEnoughInputs] if there are not enough inputs yet.
     */
    fun next() = id.next(countAddedItems())

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
    sealed interface Step : BuilderNextAction, Comparable<Step> {
        /**
         * Whether the step is related to expenses or incomes.
         */
        val isExpense: Boolean

        /**
         * The type of the category that will be added in this step.
         */
        val type: CategoryType

        /**
         * The next action in the category builder.
         */
        val next: BuilderNextAction

        /**
         * Returns the next action in the category builder if there are enough inputs added.
         * Otherwise, it raises a [NotEnoughInputs] error.
         * @param currentInputs The number of inputs added in the current step.
         * @return The next action in the category builder or [NotEnoughInputs] if there are not enough inputs yet.
         */
        fun next(currentInputs: Int) = either {
            val minimumRequiredToProceed = if (isExpense) {
                MIN_EXPENSES
            } else {
                MIN_INCOMES
            }
            if (currentInputs < minimumRequiredToProceed) {
                raise(NotEnoughInputs(minimumRequiredToProceed - currentInputs))
            } else {
                next
            }
        }

        companion object {
            private const val MIN_EXPENSES = 3
            private const val MIN_INCOMES = 1
        }
    }
}

/**
 * The first step in the category builder.
 * It is used to add fixed expenses.
 */
data object FixedExpensesStep : BuilderNextAction.Step {
    override val isExpense: Boolean = true
    override val type: CategoryType = CategoryType.Fixed
    override val next: BuilderNextAction = VariableExpensesStep

    override fun compareTo(other: BuilderNextAction.Step): Int {
        return when (other) {
            is FixedExpensesStep -> 0
            is VariableExpensesStep -> -1
            is FixedIncomesStep -> -1
        }
    }
}

/**
 * The second step in the category builder.
 * It is used to add variable expenses.
 */
data object VariableExpensesStep : BuilderNextAction.Step {
    override val isExpense: Boolean = true
    override val type: CategoryType = CategoryType.Variable
    override val next: BuilderNextAction = FixedIncomesStep

    override fun compareTo(other: BuilderNextAction.Step): Int {
        return when (other) {
            is FixedExpensesStep -> 1
            is VariableExpensesStep -> 0
            is FixedIncomesStep -> -1
        }
    }
}

/**
 * The third and last step in the category builder.
 * It is used to add fixed incomes.
 */
data object FixedIncomesStep : BuilderNextAction.Step {
    override val isExpense: Boolean = false
    override val type: CategoryType = CategoryType.Fixed
    override val next: BuilderNextAction = BuilderNextAction.Complete

    override fun compareTo(other: BuilderNextAction.Step): Int {
        return when (other) {
            is FixedExpensesStep -> 1
            is VariableExpensesStep -> 1
            is FixedIncomesStep -> 0
        }
    }
}

/**
 * Represents an error that occurs when the user tries to move to the next step in the category builder but there are
 * not enough inputs yet.
 * @property remainingInputs The number of missing inputs to proceed to the next step.
 */
data class NotEnoughInputs(val remainingInputs: Int)
