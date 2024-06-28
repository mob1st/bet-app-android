package br.com.mob1st.bet.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import br.com.mob1st.core.database.TwoCentsDb
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single {
        AndroidSqliteDriver(
            schema = TwoCentsDb.Schema,
            context = get(),
            name = "twocents.db",
        )
    } bind SqlDriver::class
}
