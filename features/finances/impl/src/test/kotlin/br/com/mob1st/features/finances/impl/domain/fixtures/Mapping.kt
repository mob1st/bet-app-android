package br.com.mob1st.features.finances.impl.domain.fixtures

import br.com.mob1st.core.kotlinx.structures.toBiMap
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceType
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth

val typeRecurrenceToRecurrences = RecurrenceType.entries.map { type ->
    type to when (type) {
        RecurrenceType.Fixed -> Recurrences.Fixed(DayOfMonth(1))
        RecurrenceType.Variable -> Recurrences.Variable
        RecurrenceType.Seasonal -> Recurrences.Seasonal(emptyList())
    }
}.toBiMap()

val allStepsAndNextActions: Map<BudgetBuilderAction.Step, BudgetBuilderAction> = mapOf(
    VariableExpensesStep to FixedExpensesStep,
    FixedExpensesStep to SeasonalExpensesStep,
    SeasonalExpensesStep to FixedIncomesStep,
    FixedExpensesStep to BudgetBuilderAction.Complete,
)
