package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertEquals

internal class SelectCategoriesByStepArgsTest {
    @ParameterizedTest
    @ArgumentsSource(StepsProvider::class)
    fun `GIVEN a step WHEN get arguments THEN assert return is expected`(
        step: BuilderNextAction.Step,
        isExpense: Boolean,
        recurrenceTypeDescription: String,
    ) {
        // When
        val actual = SelectCategoriesByStepArgs.from(step)
        // Then
        val expected = SelectCategoriesByStepArgs(
            isExpense = isExpense,
            recurrenceTypeDescription = recurrenceTypeDescription,
        )
        assertEquals(
            expected,
            actual,
        )
    }

    object StepsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments>? {
            return Stream.of(
                Arguments.of(FixedIncomesStep, false, RecurrenceType.Fixed.value),
                Arguments.of(FixedExpensesStep, true, RecurrenceType.Fixed.value),
                Arguments.of(VariableExpensesStep, true, RecurrenceType.Variable.value),
                Arguments.of(SeasonalExpensesStep, true, RecurrenceType.Seasonal.value),
            )
        }
    }
}
