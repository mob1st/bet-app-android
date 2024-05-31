package br.com.mob1st.features.finances.impl.data.db

import br.com.mob1st.features.finances.impl.PorkyDb

internal class CategoryUpdate(private val porkyDb: PorkyDb) {
    operator fun set(
        id: String,
        name: String,
    ) {
        porkyDb.categoryQueries.update(
            name = name,
            id = id.toLong(),
        )
    }
}
