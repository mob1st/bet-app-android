package br.com.mob1st.features.finances.impl.ui.builder.navigation

import android.os.Bundle
import androidx.core.os.bundleOf
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(ParameterizedRobolectricTestRunner::class)
internal class BuilderStepNavTypeTest(
    private val step: BudgetBuilderAction.Step,
    private val stepAsString: String,
) {
    internal val type = BuilderStepNavType

    @Test
    fun `GIVEN a step WHEN get from bundle THEN s`() {
        val bundle = bundleOf("step" to stepAsString)
        val actual = BuilderStepNavType[bundle, "step"]
        assertEquals(step, actual)
    }

    @Test
    fun `GIVEN a step WHEN parse value THEN return step as string`() {
        val actual = BuilderStepNavType.parseValue(stepAsString)
        assertEquals(step, actual)
    }

    @Test
    fun `GIVEN a step WHEN put in bundle THEN return step as string`() {
        val bundle = Bundle()
        BuilderStepNavType.put(bundle, "step", step)
        val actual = bundle.getString("step")
        assertEquals(stepAsString, actual)
    }

    @Test
    fun `GIVEN a step WHEN serialize as value THEN return step as string`() {
        val actual = BuilderStepNavType.serializeAsValue(step)
        assertEquals(stepAsString, actual)
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters
        fun source() = listOf(
            arrayOf(
                FixedExpensesStep,
                "FixedExpenses",
            ),
            arrayOf(
                VariableExpensesStep,
                "VariableExpenses",
            ),
            arrayOf(
                SeasonalExpensesStep,
                "SeasonalExpenses",
            ),
            arrayOf(
                FixedIncomesStep,
                "FixedIncomes",
            ),
        )
    }
}
