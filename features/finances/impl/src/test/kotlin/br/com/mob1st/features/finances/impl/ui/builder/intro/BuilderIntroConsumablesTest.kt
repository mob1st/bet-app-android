package br.com.mob1st.features.finances.impl.ui.builder.intro

import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.ui.fixtures.builderStepToNavArgsMap
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.next
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BuilderIntroConsumablesTest {
    @Test
    fun `GIVEN a step WHEN navigate to step THEN assert navigation is set`() {
        val step = Arb.bind<BudgetBuilderAction.Step>().next()
        val consumables = BuilderIntroConsumables()
        val actual = consumables.navigateToStep(step)
        val expected = BuilderIntroConsumables(
            navEvent = BuilderIntroNextStepNavEvent(builderStepToNavArgsMap.getRightValue(step)),
        )
        assertEquals(expected, actual)
    }
}
