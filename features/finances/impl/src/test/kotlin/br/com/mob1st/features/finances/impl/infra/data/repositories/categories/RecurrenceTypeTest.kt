package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertFailsWith

internal class RecurrenceTypeTest {
    @ParameterizedTest
    @ArgumentsSource(ValueProvider::class)
    fun `GIVEN a value WHEN get from it THEN assert return equals`(
        value: String,
        expected: RecurrenceType,
    ) {
        val actual = RecurrenceType.fromValue(value)
        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @ArgumentsSource(ValueProvider::class)
    fun `GIVEN a value And a recurrence type WHEN get enum value THEN assert return equals`(
        value: String,
        recurrenceType: RecurrenceType,
    ) {
        assertEquals(value, recurrenceType.value)
    }

    @Test
    fun `GIVEN an unknown value WHEN get from it THEN assert throws exception`() {
        val value = "unknown"
        assertFailsWith<IllegalArgumentException> {
            RecurrenceType.fromValue(value)
        }
    }

    object ValueProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments>? {
            return Stream.of(
                Arguments.of("fixed", RecurrenceType.Fixed),
                Arguments.of("variable", RecurrenceType.Variable),
                Arguments.of("seasonal", RecurrenceType.Seasonal),
            )
        }
    }
}
