package br.com.mob1st.features.finances.impl.data.morphisms

import br.com.mob1st.core.kotlinx.monad.Morphism
import br.com.mob1st.features.finances.impl.data.preferences.RecurrenceBuilderCompletions
import br.com.mob1st.features.finances.impl.data.ram.RecurrenceBuilderLists
import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceBuilder
import kotlinx.collections.immutable.toPersistentList

/**
 * Cache representation of [RecurrenceBuilder].
 * Since its data is split between ram cache and disk cache, we use a pair to be able to persist it separately.
 */
internal typealias RecurrenceBuilderCache = Pair<RecurrenceBuilderCompletions, RecurrenceBuilderLists>

/**
 * Isomorphism between [RecurrenceBuilder] and [RecurrenceBuilderCache]
 */
internal fun recurrenceBuilderIsomorphism() = toDomain() to toCache()

private fun toDomain() = Morphism<RecurrenceBuilderCache, RecurrenceBuilder> { (preferences, lists) ->
    RecurrenceBuilder(
        fixedExpensesStep = RecurrenceBuilder.Step(
            list = lists.fixedExpensesList,
            isCompleted = preferences.isFixedExpansesCompleted
        ),
        variableExpensesStep = RecurrenceBuilder.Step(
            list = lists.variableExpensesList,
            isCompleted = preferences.isVariableExpansesCompleted
        ),
        seasonalExpensesStep = RecurrenceBuilder.Step(
            list = lists.seasonalExpensesList,
            isCompleted = preferences.isSeasonalExpansesCompleted
        ),
        incomesStep = RecurrenceBuilder.Step(
            list = lists.incomesList,
            isCompleted = preferences.isIncomesCompleted
        )
    )
}

private fun toCache() = Morphism<RecurrenceBuilder, RecurrenceBuilderCache> { builder ->
    builder.toPreferences() to builder.toLists()
}

private fun RecurrenceBuilder.toLists(): RecurrenceBuilderLists {
    return RecurrenceBuilderLists(
        fixedExpensesList = fixedExpensesStep.list.toPersistentList(),
        variableExpensesList = variableExpensesStep.list.toPersistentList(),
        seasonalExpensesList = seasonalExpensesStep.list.toPersistentList(),
        incomesList = incomesStep.list.toPersistentList()
    )
}

private fun RecurrenceBuilder.toPreferences(): RecurrenceBuilderCompletions {
    return RecurrenceBuilderCompletions(
        isFixedExpansesCompleted = fixedExpensesStep.isCompleted,
        isVariableExpansesCompleted = variableExpensesStep.isCompleted,
        isSeasonalExpansesCompleted = seasonalExpensesStep.isCompleted,
        isIncomesCompleted = incomesStep.isCompleted
    )
}
