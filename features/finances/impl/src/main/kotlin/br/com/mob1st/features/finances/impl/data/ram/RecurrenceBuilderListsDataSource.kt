package br.com.mob1st.features.finances.impl.data.ram

import br.com.mob1st.core.data.UnitaryDataSource
import br.com.mob1st.core.kotlinx.coroutines.DefaultCoroutineDispatcher
import br.com.mob1st.features.finances.impl.data.system.RecurrenceLocalizationProvider
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update

/**
 * Stateful data source for the lists of RecurrentCategory that are being built by the user for each step in the
 * builder.
 *
 * Ensure this is a singleton attached to the builder feature scope.
 * By default, the initial value is the recurrent category suggestions defined in the [RecurrenceSuggestionFactory]
 * implementations.
 */
internal class RecurrenceBuilderListsDataSource(
    localizationProvider: RecurrenceLocalizationProvider,
    default: DefaultCoroutineDispatcher,
) : UnitaryDataSource<RecurrenceBuilderLists> {
    private val state = MutableStateFlow(RecurrenceBuilderLists())
    override val data: Flow<RecurrenceBuilderLists> =
        flow {
            state.update {
                it.copy(
                    fixedExpensesList = FixedExpanseFactory(localizationProvider).toPersistentList(),
                    variableExpensesList = VariableExpanseFactory(localizationProvider).toPersistentList(),
                    seasonalExpensesList = SeasonalExpanseFactory(localizationProvider).toPersistentList(),
                    incomesList = IncomeFactory(localizationProvider).toPersistentList(),
                )
            }
            emitAll(state)
        }.flowOn(default)

    override suspend fun set(data: RecurrenceBuilderLists) {
        state.value = data
    }
}
