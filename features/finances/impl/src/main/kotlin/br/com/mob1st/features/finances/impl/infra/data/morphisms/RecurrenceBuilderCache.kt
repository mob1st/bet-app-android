package br.com.mob1st.features.finances.impl.infra.data.morphisms

import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceBuilder
import br.com.mob1st.features.finances.impl.infra.data.preferences.RecurrenceBuilderCompletions
import br.com.mob1st.features.finances.impl.infra.data.ram.RecurrenceBuilderLists
import kotlinx.collections.immutable.toPersistentList

/**
 * Cache representation of [RecurrenceBuilder].
 * Since its data is split between ram cache and disk cache, we use a pair to be able to persist it separately.
 * @property completions The completion state of each step
 * @property lists The list of each step
 */
internal data class RecurrenceBuilderCache(
    val completions: RecurrenceBuilderCompletions,
    val lists: RecurrenceBuilderLists,
)

/**
 * Convert [RecurrenceBuilderCache] to [RecurrenceBuilder]
 */
internal fun br.com.mob1st.features.finances.impl.infra.data.morphisms.RecurrenceBuilderCache.toDomain() =
    RecurrenceBuilder(
        fixedExpensesStep =
        RecurrenceBuilder.Step(
            isCompleted = completions.isFixedExpansesCompleted,
            list = lists.fixedExpensesList,
        ),
        variableExpensesStep =
        RecurrenceBuilder.Step(
            isCompleted = completions.isVariableExpansesCompleted,
            list = lists.variableExpensesList,
        ),
        seasonalExpensesStep =
        RecurrenceBuilder.Step(
            isCompleted = completions.isSeasonalExpansesCompleted,
            list = lists.seasonalExpensesList,
        ),
        incomesStep =
        RecurrenceBuilder.Step(
            isCompleted = completions.isIncomesCompleted,
            list = lists.incomesList,
        ),
    )

/**
 * Convert [RecurrenceBuilder] to [RecurrenceBuilderCache]
 */
internal fun RecurrenceBuilder.toData() =
    br.com.mob1st.features.finances.impl.infra.data.morphisms.RecurrenceBuilderCache(
        completions = toPreferences(),
        lists = toLists(),
    )

private fun RecurrenceBuilder.toLists(): RecurrenceBuilderLists {
    return RecurrenceBuilderLists(
        fixedExpensesList = fixedExpensesStep.list.toPersistentList(),
        variableExpensesList = variableExpensesStep.list.toPersistentList(),
        seasonalExpensesList = seasonalExpensesStep.list.toPersistentList(),
        incomesList = incomesStep.list.toPersistentList(),
    )
}

private fun RecurrenceBuilder.toPreferences(): RecurrenceBuilderCompletions {
    return RecurrenceBuilderCompletions(
        isFixedExpansesCompleted = fixedExpensesStep.isCompleted,
        isVariableExpansesCompleted = variableExpensesStep.isCompleted,
        isSeasonalExpansesCompleted = seasonalExpensesStep.isCompleted,
        isIncomesCompleted = incomesStep.isCompleted,
    )
}
