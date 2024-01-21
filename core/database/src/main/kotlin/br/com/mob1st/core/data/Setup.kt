package br.com.mob1st.core.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import br.com.mob1st.core.database.PorkyDb

fun doIt(context: Context): SqlDriver = AndroidSqliteDriver(PorkyDb.Schema, context, "porky.db")
