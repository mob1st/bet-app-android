package br.com.mob1st.tests.featuresutils

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.core.kotlinx.structures.Uuid
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import com.appmattus.kotlinfixture.kotlinFixture
import java.util.UUID

/**
 * Returns a random enum value.
 */
inline fun <reified T : Enum<T>> randomEnum(): T {
    val values = enumValues<T>()
    return values.random()
}

/**
 * Default implementation of [kotlinFixture] with some common fixtures that cannot be created by the library, like
 * value classes and UUIDs.
 * Prefer to use it as a base for a more specific fixture factory instead of using it directly.
 */
val defaultFixtures = kotlinFixture {
    factory<Uuid> {
        Uuid()
    }
    factory<RowId> {
        RowId(UUID.randomUUID().mostSignificantBits)
    }
    factory<Money> {
        Money((0..Long.MAX_VALUE).random())
    }
}

/**
 * Creates a fixture for the given type.
 * @param configuration the configuration for the fixture
 * @return a random fixture
 */
inline fun <reified T> fixture(
    noinline configuration: ConfigurationBuilder.() -> Unit = {},
): T {
    return defaultFixtures<T>(configuration = configuration)
}

/**
 * Creates a list of fixtures for the given type, adding items until the given size is reached.
 * @param size the size of the list
 * @return a list of fixtures
 */
inline fun <reified T> fixtureList(size: Int = 5): List<T> {
    val kotlinFixture = kotlinFixture {
        repeatCount { size }
    }
    return kotlinFixture<List<T>>()
}
