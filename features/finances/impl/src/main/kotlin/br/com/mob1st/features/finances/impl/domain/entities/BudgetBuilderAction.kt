package br.com.mob1st.features.finances.impl.domain.entities

/**
 * Represents the action that the user can take in the category builder.
 */
sealed interface BudgetBuilderAction {
    /**
     * Action that completes the category builder and allows the user to proceed to the main area of the app.
     */
    data object Complete : BudgetBuilderAction

    /**
     * A specific step in the category builder.
     */
    sealed interface Step : BudgetBuilderAction {
        /**
         * The minimum number of inputs required to proceed to the next step.
         */
        val minimumRequiredToProceed: Int

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
data object FixedExpensesStep : BudgetBuilderAction.Step {
    private const val REQUIRED_INPUTS = 3
    override val minimumRequiredToProceed: Int = REQUIRED_INPUTS
    override val type: RecurrenceType = RecurrenceType.Fixed
    override val isExpense: Boolean = true
}

/**
 * The second step in the category builder.
 * It is used to add variable expenses.
 */
data object VariableExpensesStep : BudgetBuilderAction.Step {
    private const val REQUIRED_INPUTS = 2
    override val minimumRequiredToProceed: Int = REQUIRED_INPUTS
    override val type: RecurrenceType = RecurrenceType.Variable
    override val isExpense: Boolean = true
}

/**
 * The third step in the category builder.
 * It is used to add seasonal expenses.
 */
data object SeasonalExpensesStep : BudgetBuilderAction.Step {
    override val minimumRequiredToProceed: Int = 0
    override val type: RecurrenceType = RecurrenceType.Seasonal
    override val isExpense: Boolean = true
}

/**
 * The fourth step in the category builder.
 * It is used to add fixed incomes.
 */
data object FixedIncomesStep : BudgetBuilderAction.Step {
    override val minimumRequiredToProceed: Int = 0
    override val type: RecurrenceType = RecurrenceType.Fixed
    override val isExpense: Boolean = false
}
