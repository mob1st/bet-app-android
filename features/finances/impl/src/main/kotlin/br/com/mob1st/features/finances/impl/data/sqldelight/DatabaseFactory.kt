package br.com.mob1st.features.finances.impl.data.sqldelight

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import br.com.mob1st.core.database.FixedRecurrences
import br.com.mob1st.core.database.SeasonalRecurrences
import br.com.mob1st.core.database.VariableRecurrences
import br.com.mob1st.features.finances.impl.TwoCentsDb

/**
 *
 */
object DatabaseFactory {
    fun create(driver: SqlDriver): TwoCentsDb {
        return TwoCentsDb(
            driver = driver,
            FixedRecurrencesAdapter = FixedRecurrences.Adapter(
                day_of_monthAdapter = IntColumnAdapter,
            ),
            VariableRecurrencesAdapter = VariableRecurrences.Adapter(
                day_of_weekAdapter = IntColumnAdapter,
            ),
            SeasonalRecurrencesAdapter = SeasonalRecurrences.Adapter(
                dayAdapter = IntColumnAdapter,
                monthAdapter = IntColumnAdapter,
            ),
        )
    }
}
