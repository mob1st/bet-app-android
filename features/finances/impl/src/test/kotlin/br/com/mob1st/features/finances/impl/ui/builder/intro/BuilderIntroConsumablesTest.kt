package br.com.mob1st.features.finances.impl.ui.builder.intro

import br.com.mob1st.features.utils.errors.CommonError
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BuilderIntroConsumablesTest {
    @Test
    fun `GIVEN a exception WHEN handle error THEN assert it become a common error`() {
        val throwable = Throwable("Error")
        val consumables = BuilderIntroConsumables()
        val actual = consumables.handleError(throwable)
        val expected = BuilderIntroConsumables(error = CommonError.Unknown)
        assertEquals(expected, actual)
    }
}
