package br.com.mob1st.features.finances.impl.infra.data.sqldelight

import app.cash.sqldelight.db.SqlDriver
import br.com.mob1st.features.finances.impl.TwoCentsDb

/**
 * Factory for creating the database instance.
 */
object DatabaseFactory {
    /**
     * Creates the database instance.
     * @param driver the SQL driver
     * @return the database instance
     */
    fun create(driver: SqlDriver): TwoCentsDb {
        return TwoCentsDb(driver = driver)
    }
}
