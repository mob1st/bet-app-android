package br.com.mob1st.features.finances.impl.domain.fixtures

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.Asset
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategoryDetail
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfYear
import br.com.mob1st.features.finances.impl.infra.data.fixtures.rowId
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.merge
import io.kotest.property.arbitrary.next
import io.kotest.property.arbs.domain
import io.kotest.property.arbs.products.products

fun Arb.Companion.categoryDetail(): Arb<CategoryDetail> {
    return Arb.bind {
        bind(CategoryDetail::category to category())
    }
}

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

/**
 * Generates a valid [Money] instance
 */
fun Arb.Companion.money(): Arb<Money> {
    return Arb.products().map { Money(it.price.toLong()) }
}

/**
 * Generates a valid [Uri] instance
 */
fun Arb.Companion.uri(): Arb<Uri> {
    return Arb.domain().filter { it.value.isNotBlank() }.map { Uri(it.value) }
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

/**
 * Generates a valid [Recurrences] instance
 */
fun Arb.Companion.fixedRecurrences(): Arb<Recurrences.Fixed> {
    return Arb.dayOfMonth().map { Recurrences.Fixed(it) }
}

/**
 * Generates a valid [Recurrences] instance
 */
fun Arb.Companion.variableRecurrences(): Arb<Recurrences.Variable> {
    return arbitrary { Recurrences.Variable }
}

/**
 * Generates a valid [Recurrences] instance
 */
fun Arb.Companion.seasonalRecurrences(): Arb<Recurrences.Seasonal> {
    return Arb.dayOfYear().chunked(0..4).map { Recurrences.Seasonal(it) }
}

/**
 * Generates a valid [DayOfMonth] instance
 */
fun Arb.Companion.dayOfMonth(): Arb<DayOfMonth> {
    return Arb.int(1..31).map { DayOfMonth(it) }
}

/**
 * Generates a valid [DayOfYear] instance
 */
fun Arb.Companion.dayOfYear(): Arb<DayOfYear> {
    return Arb.int(1..365).map { DayOfYear(it) }
}

/**
 * Generates a valid [BudgetBuilder] instance
 */
fun Arb.Companion.budgetBuilder(): Arb<BudgetBuilder> {
    return Arb.bind {
        bind(Category::class to category())
    }
}

/**
 * Generates a valid [BudgetBuilderAction] instance
 */
fun Arb.Companion.builderNextAction(): Arb<BudgetBuilderAction> {
    return arbitrary { BudgetBuilderAction.Complete }.merge(
        Arb.bind<BudgetBuilderAction.Step>(),
    )
}

/**
 * Generates a valid [CategorySuggestion] instance
 */
fun Arb.Companion.categorySuggestion(): Arb<CategorySuggestion> {
    return Arb.bind {
        bind(CategorySuggestion::image to uri())
    }
}

/**
 * Generates a valid [Asset] instance
 */
fun Arb.Companion.asset(): Arb<Asset> {
    return Arb.bind {
        bind(Asset::uri to uri())
    }
}
