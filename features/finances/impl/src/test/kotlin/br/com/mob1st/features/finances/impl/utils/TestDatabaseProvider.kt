package br.com.mob1st.features.finances.impl.utils

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.cash.sqldelight.logs.LogSqliteDriver
import br.com.mob1st.features.finances.impl.TwoCentsDb
import br.com.mob1st.features.finances.impl.infra.data.sqldelight.DatabaseFactory

/**
 * Creates an in-memory database instance for tests, initializing the schema with the same driver that will
 * be used in the database creation.
 * @return the database instance
 */
fun testTwoCentsDb(): TwoCentsDb {
    val sqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    val logDriver = LogSqliteDriver(sqlDriver) {
        println(it)
    }
    TwoCentsDb.Schema.create(sqlDriver)
    return DatabaseFactory.create(logDriver)
}
