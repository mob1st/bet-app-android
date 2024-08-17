package br.com.mob1st.features.finances.impl.ui.fixtures

import br.com.mob1st.features.finances.impl.domain.fixtures.money
import br.com.mob1st.features.finances.impl.domain.fixtures.recurrences
import br.com.mob1st.features.finances.impl.domain.fixtures.uri
import br.com.mob1st.features.finances.impl.ui.category.detail.CategoryEntry
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.map
import io.kotest.property.arbs.products.products

fun Arb.Companion.categoryEntry(): Arb<CategoryEntry> {
    return Arb.bind<CategoryEntry> {
        bind(CategoryEntry::amount to money())
        bind(CategoryEntry::image to uri())
        bind(CategoryEntry::recurrences to recurrences())
        bind(CategoryEntry::name to Arb.products().map { it.name })
    }
}
