package br.com.mob1st.tests.featuresutils

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uuid
import com.appmattus.kotlinfixture.config.ConfigurationBuilder
import com.appmattus.kotlinfixture.kotlinFixture

/**
 * Returns a random enum value.
 */
inline fun <reified T : Enum<T>> randomEnum(): T {
    val values = enumValues<T>()
    return values.random()
}

/**
 * Generates a random boolean value.
 * @return random boolean value.
 */
fun randomBoolean() = (0..1).random() == 1

/**
 * Creates a fixture for the given type.
 * @param configuration the configuration for the fixture
 * @return a random fixture
 */
inline fun <reified T> fixture(noinline configuration: ConfigurationBuilder.() -> Unit = {}): T {
    val kotlinFixture = kotlinFixture {
        factory<Uuid> {
            Uuid()
        }
        factory<Money> {
            Money.Zero
        }
    }
    return kotlinFixture<T>(configuration = configuration)
}

/**
 * Creates a list of fixtures for the given type, adding items until the given size is reached.
 * @param size the size of the list
 * @return a list of fixtures
 */
inline fun <reified T> fixtureList(size: Int = 5): List<T> {
    val kotlinFixture =
        kotlinFixture {
            repeatCount { size }
        }
    return kotlinFixture<List<T>>()
}
