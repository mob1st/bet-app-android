package br.com.mob1st.features.finances.impl.data.ram

import br.com.mob1st.core.data.UnitaryDataSource
import br.com.mob1st.core.kotlinx.coroutines.DefaultCoroutineDispatcher
import br.com.mob1st.features.finances.impl.domain.providers.RecurrenceLocalizationProvider
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update

internal class RecurrenceBuilderListsDataSource(
    localizationProvider: RecurrenceLocalizationProvider,
    default: DefaultCoroutineDispatcher,
) : UnitaryDataSource<RecurrenceBuilderLists> {

    private val state = MutableStateFlow(RecurrenceBuilderLists())
    override val data: Flow<RecurrenceBuilderLists> = flow {
        state.update {
            it.copy(
                fixedExpensesList = FixedExpanseFactory(localizationProvider).toPersistentList(),
                variableExpensesList = VariableExpanseFactory(localizationProvider).toPersistentList(),
                seasonalExpensesList = SeasonalExpanseFactory(localizationProvider).toPersistentList(),
                incomesList = IncomeFactory(localizationProvider).toPersistentList()
            )
        }
        emitAll(state)
    }.flowOn(default)

    override suspend fun set(data: RecurrenceBuilderLists) {
        state.value = data
    }
}
