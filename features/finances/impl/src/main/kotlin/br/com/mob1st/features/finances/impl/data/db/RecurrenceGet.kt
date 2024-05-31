package br.com.mob1st.features.finances.impl.data.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import br.com.mob1st.features.finances.impl.PorkyDb
import kotlin.coroutines.CoroutineContext

internal class RecurrenceGet(
    private val porkyDb: PorkyDb,
) {
    operator fun invoke(context: CoroutineContext) =
        porkyDb.recurrentCategoryQueries
            .selectLastInsertedId()
            .asFlow()
            .mapToList(context)
}
