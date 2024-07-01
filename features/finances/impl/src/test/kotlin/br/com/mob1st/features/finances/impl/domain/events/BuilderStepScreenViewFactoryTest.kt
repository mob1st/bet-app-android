package br.com.mob1st.features.finances.impl.domain.events

import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class BuilderStepScreenViewFactoryTest {
    @ParameterizedTest
    @ArgumentsSource(StepPerEventKeyProvider::class)
    fun `GIVEN a step WHEN create screen view event THEN return the correct event key`(
        step: BuilderNextAction.Step,
        expectedEventKey: String,
    ) {
        val event = BuilderStepScreenViewFactory.create(step)
        assertEquals(event.screenName, expectedEventKey)
    }

    object StepPerEventKeyProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(FixedExpensesStep, "builder_fixed_expenses"),
                Arguments.of(VariableExpensesStep, "builder_variable_expenses"),
                Arguments.of(FixedIncomesStep, "builder_fixed_incomes"),
            )
        }
    }
}
