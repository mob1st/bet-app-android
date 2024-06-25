package br.com.mob1st.core.data

import android.content.Context
import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import br.com.mob1st.core.database.TwoCentsDb

object DatabaseProvider {
    fun create(context: Context): TwoCentsDb {
        val driver: SqlDriver = AndroidSqliteDriver(
            schema = TwoCentsDb.Schema,
            context = context,
            name = "twocents.db",
        )
        return TwoCentsDb(
            driver = driver,
        )
    }
}

class MyAdapter : ColumnAdapter<String, Int> {
    override fun decode(databaseValue: Int): String {
        TODO("Not yet implemented")
    }

    override fun encode(value: String): Int {
        TODO("Not yet implemented")
    }
}
