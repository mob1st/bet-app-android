package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType

data class CategoryBuilder(
    val step: BuilderNextAction.Step,
    val manuallyAdded: List<Category>,
    val suggestions: List<CategorySuggestion>,
) {
    fun next(): BuilderNextAction {
        return when (step) {
            BuilderNextAction.Step.FIXED_EXPENSES -> BuilderNextAction.Step.VARIABLE_EXPENSES
            BuilderNextAction.Step.VARIABLE_EXPENSES -> BuilderNextAction.Step.FIXED_INCOMES
            BuilderNextAction.Step.FIXED_INCOMES -> BuilderNextAction.Complete
        }
    }

    companion object {
        fun firstStep() = BuilderNextAction.Step.FIXED_EXPENSES
    }
}

sealed interface BuilderNextAction {
    data object Complete : BuilderNextAction

    enum class Step : BuilderNextAction {
        FIXED_EXPENSES,
        VARIABLE_EXPENSES,
        FIXED_INCOMES,
    }
}

internal fun BuilderNextAction.Step.toParameter(): Pair<CategoryType, Boolean> {
    return when (this) {
        BuilderNextAction.Step.FIXED_EXPENSES -> CategoryType.Fixed to true
        BuilderNextAction.Step.VARIABLE_EXPENSES -> CategoryType.Variable to true
        BuilderNextAction.Step.FIXED_INCOMES -> CategoryType.Fixed to false
    }
}
