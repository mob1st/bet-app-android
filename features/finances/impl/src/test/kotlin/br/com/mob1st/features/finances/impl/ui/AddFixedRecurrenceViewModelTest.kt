package br.com.mob1st.features.finances.impl.ui

import br.com.mob1st.features.finances.impl.fakes.FakeGetFixedExpensesUseCase
import br.com.mob1st.tests.featuresutils.fixture
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddFixedRecurrenceViewModelTest {

    private lateinit var subject: AddFixedRecurrenceViewModel

    private lateinit var getFixedExpensesUseCase: FakeGetFixedExpensesUseCase

    @BeforeEach
    fun setUp() {
        getFixedExpensesUseCase = FakeGetFixedExpensesUseCase()
        subject = AddFixedRecurrenceViewModel(
            getFixedExpensesUseCase = getFixedExpensesUseCase
        )
    }

    @Test
    fun `GIVEN list of fixed expenses WHEN get ui state THEN assert list of items`() = runTest {
        // GIVEN
        getFixedExpensesUseCase.invokeState.value = fixture()
        // WHEN
        val actual = getFixedExpensesUseCase().first()

        // THEN
        assertEquals(
            getFixedExpensesUseCase.invokeState.value,
            actual
        )
    }
}
