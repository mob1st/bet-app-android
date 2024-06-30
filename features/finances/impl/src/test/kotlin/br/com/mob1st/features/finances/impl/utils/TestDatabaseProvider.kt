package br.com.mob1st.features.finances.impl.utils

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.cash.sqldelight.logs.LogSqliteDriver
import br.com.mob1st.features.finances.impl.TwoCentsDb
import br.com.mob1st.features.finances.impl.data.sqldelight.DatabaseFactory

/**
 * Creates an in-memory database instance for tests, initializing the schema with the same driver that will
 * be used in the database creation.
 * @return the database instance
 */
fun testDatabase(): TwoCentsDb {
    val driver = LogSqliteDriver(JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)) {
        println(it)
    }
    TwoCentsDb.Schema.create(driver)
    return DatabaseFactory.create(driver)
}
