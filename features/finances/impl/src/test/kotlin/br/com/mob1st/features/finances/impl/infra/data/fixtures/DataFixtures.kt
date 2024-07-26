package br.com.mob1st.features.finances.impl.infra.data.fixtures

import br.com.mob1st.core.database.Categories
import br.com.mob1st.features.finances.impl.infra.data.repositories.categories.RecurrenceColumns
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.localDateTime
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.uuid
import io.kotest.property.arbs.domain
import io.kotest.property.arbs.products.products
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Generates a valid row id
 */
fun Arb.Companion.rowId() = Arb.uuid().map { it.leastSignificantBits }

/**
 * Generates a valid categories entity
 */
fun Arb.Companion.categories(): Arb<Categories> {
    return Arb.bind<Categories> {
        bind(Categories::id to rowId())
        bind(Categories::image to domain().map { it.value })
        bind(Categories::created_at to timestamp())
        bind(Categories::updated_at to timestamp().orNull())
    }.map { categories ->
        val recurrences = recurrenceColumns().next()
        val product = products().next()
        categories.copy(
            name = product.name,
            amount = product.price.toLong(),
            recurrence_type = recurrences.rawType,
            recurrences = recurrences.rawRecurrences,
        )
    }
}

/**
 * Generates a valid [RecurrenceColumns] entity.
 * It ensures that the [RecurrenceColumns.rawRecurrences] is consistent with the [RecurrenceColumns.rawType]
 */
internal fun Arb.Companion.recurrenceColumns(): Arb<RecurrenceColumns> = arbitrary {
    listOf("fixed", "variable", "seasonal").random(it.random)
}.flatMap { type ->
    when (type) {
        "fixed" -> fixedRecurrences()
        "variable" -> arbitrary<String?> { null }
        "seasonal" -> seasonalRecurrences()
        else -> error("Invalid recurrence type")
    }.map { recurrences ->
        RecurrenceColumns(rawType = type, rawRecurrences = recurrences)
    }
}

private fun Arb.Companion.fixedRecurrences(): Arb<String> = Arb.int(1..31).map {
    String.format(Locale.getDefault(), "%02d", it)
}

private fun Arb.Companion.seasonalRecurrences(): Arb<String?> = Arb
    .int(1..366)
    .chunked(1..4)
    .orNull()
    .map { daysOfYear ->
        daysOfYear?.joinToString(",") { day ->
            String.format(Locale.getDefault(), "%03d", day)
        }
    }

fun Arb.Companion.timestamp(): Arb<String> = Arb.localDateTime().map {
    it.format(DateTimeFormatter.ISO_DATE_TIME)
}
