package br.com.mob1st.features.finances.impl.ui.fixtures

import br.com.mob1st.core.kotlinx.structures.toBiMap
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNavArgs

val builderStepToNavArgsMap = BuilderStepNavArgs.entries.map { args ->
    args to when (args) {
        BuilderStepNavArgs.FixedExpensesStepArgs -> FixedExpensesStep
        BuilderStepNavArgs.VariableExpensesStepArgs -> VariableExpensesStep
        BuilderStepNavArgs.SeasonalExpensesStepArgs -> SeasonalExpensesStep
        BuilderStepNavArgs.FixedIncomesStepArgs -> FixedIncomesStep
    }
}.toBiMap()
