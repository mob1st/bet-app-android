package br.com.mob1st.tests.featuresutils

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uri
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
 * Default implementation of [kotlinFixture] with some common fixtures that cannot be created by the library, like
 * value classes and UUIDs.
 * Prefer to use it as a base for a more specific fixture factory instead of using it directly.
 */
val defaultFixtures = kotlinFixture {
    factory<Uuid> {
        Uuid()
    }
    factory<Money> {
        Money((0..Long.MAX_VALUE).random())
    }
    factory<Uri> {
        val random = fixture<String>()
        Uri("test://$random")
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
 * Randomly updates an item in the list.
 * @param block The block to update the item.
 * @return The updated list.
 */
fun <T> List<T>.randomUpdate(block: (T) -> T): List<T> {
    val mutable = toMutableList()
    val index = mutable.indices.random()
    val pickedItem = mutable[index]
    mutable[index] = block(pickedItem)
    return mutable
}
