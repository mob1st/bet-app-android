package br.com.mob1st.features.finances.impl.domain.fixtures

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.infra.data.fixtures.rowId
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.merge
import io.kotest.property.arbitrary.next
import io.kotest.property.arbs.domain
import io.kotest.property.arbs.products.products

/**
 * Generates a valid category entity
 */
fun Arb.Companion.category(): Arb<Category> {
    return Arb.bind<Category> {
        bind(Category::id to rowId().map { Category.Id(it) })
        bind(Category::image to uri())
        bind(Category::recurrences to recurrences())
    }.map { category ->
        val product = Arb.products().next()
        category.copy(
            name = product.name,
            amount = Money(product.price.toLong()),
        )
    }
}

fun Arb.Companion.uri(): Arb<Uri> {
    return Arb.domain().map { Uri(it.value) }
}

/**
 * Generates a valid [Recurrences] instance
 */
fun Arb.Companion.recurrences(): Arb<Recurrences> {
    return Arb
        .fixedRecurrences()
        .merge(seasonalRecurrences())
        .merge(variableRecurrences())
}

fun Arb.Companion.fixedRecurrences(): Arb<Recurrences.Fixed> {
    return Arb.dayOfMonth().map { Recurrences.Fixed(it) }
}

fun Arb.Companion.variableRecurrences(): Arb<Recurrences.Variable> {
    return arbitrary { Recurrences.Variable }
}

fun Arb.Companion.seasonalRecurrences(): Arb<Recurrences.Seasonal> {
    return Arb.dayOfYear().chunked(0..4).map { Recurrences.Seasonal(it) }
}

fun Arb.Companion.dayOfMonth(): Arb<DayOfMonth> {
    return Arb.int(1..31).map { DayOfMonth(it) }
}

fun Arb.Companion.dayOfYear(): Arb<DayOfYear> {
    return Arb.int(1..365).map { DayOfYear(it) }
}
