package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.lifecycle.ViewModel
import app.cash.turbine.test
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.tests.featuresutils.FakeErrorHandler
import br.com.mob1st.tests.featuresutils.fixture
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertTrue

class CategorySheetDelegateTest {
    private lateinit var delegate: CategorySheetDelegate
    private lateinit var errorHandler: FakeErrorHandler
    private lateinit var receiverViewModel: ReceiverViewModel

    @BeforeEach
    fun setup() {
        errorHandler = FakeErrorHandler()
        delegate = CategorySheetDelegate(errorHandler)
        receiverViewModel = ReceiverViewModel()
    }

    @Test
    fun `GIVEN a initial state WHEN collect THEN assert the initial state is null`() = runTest {
        // WHEN
        delegate.sheetOutput.test {
            // THEN
            assertNull(awaitItem())
        }
    }

    @Test
    fun `GIVEN any sheet state WHEN set THEN assert the sheet is shown`() = runTest {
        // GIVEN
        val sheetState = fixture<CategorySheetState>()

        // WHEN
        receiverViewModel.showSheet(sheetState)

        // THEN
        delegate.sheetOutput.test {
            assertEquals(
                sheetState,
                awaitItem(),
            )
        }
    }

    @Test
    fun `GIVEN any sheet state WHEN set category amount THEN assert the amount is set`() = runTest {
        // GIVEN
        val sheetState = fixture<CategorySheetState>()
        receiverViewModel.showSheet(sheetState)

        // WHEN
        receiverViewModel.showSheet(sheetState)
        delegate.setCategoryAmount("10")

        // THEN
        delegate.sheetOutput.test {
            assertEquals(
                sheetState.copy(
                    input = sheetState.input.copy(value = Money.from("10")),
                ),
                awaitItem(),
            )
        }
    }

    @Test
    fun `GIVEN any sheet state WHEN consume THEN assert the sheet is null`() = runTest {
        // GIVEN
        val sheetState = fixture<CategorySheetState>()
        receiverViewModel.showSheet(sheetState)

        // WHEN
        delegate.consumeSheet()

        // THEN
        delegate.sheetOutput.test {
            assertNull(awaitItem())
        }
    }

    @Test
    fun `GIVEN any sheet state WHEN submit category THEN assert the sheet is consumed`() = runTest {
        // GIVEN
        val sheetState = fixture<CategorySheetState>()
        receiverViewModel.showSheet(sheetState)

        // WHEN
        delegate.submitCategory()

        // THEN
        delegate.sheetOutput.test {
            assertNull(awaitItem())
        }
    }

    @Test
    fun `GIVEN an update for manual category WHEN set amount And submit category THEN assert only manual update event is emitted`() = runTest {
        // GIVEN
        val sheetState = CategorySheetState.updateManual(3, fixture())
        receiverViewModel.showSheet(sheetState)

        delegate.manualItemUpdateInput.test {
            // WHEN
            delegate.submitCategory()

            // THEN
            assertEquals(
                sheetState,
                this.expectMostRecentItem(),
            )
        }
    }

    @Test
    fun `GIVEN an update for suggested category WHEN set amount And submit category THEN assert suggested update event is emitted`() = runTest {
        // GIVEN
        val sheetState = CategorySheetState.updateSuggestion(3, fixture())
        receiverViewModel.showSheet(sheetState)

        delegate.suggestedItemUpdateInput.test {
            // WHEN
            delegate.submitCategory()

            // THEN
            assertEquals(
                sheetState,
                awaitItem(),
            )
        }
    }

    @Test
    fun `GIVEN an add operation WHEN submit category THEN assert manual update event is emitted`() = runTest {
        // GIVEN
        val sheetState = fixture<CategorySheetState>().copy(
            operation = CategorySheetState.Operation.Add,
        )
        receiverViewModel.showSheet(sheetState)

        delegate.manualItemUpdateInput.test {
            // WHEN
            delegate.submitCategory()

            // THEN
            assertEquals(
                sheetState,
                awaitItem(),
            )
        }
    }

    @Test
    fun `GIVEN an initial state WHEN set amount THEN assert error is not thrown`() = runTest {
        // WHEN
        delegate.setCategoryAmount("10")

        // THEN
        assertTrue(errorHandler.error is IllegalStateException)
    }

    @Test
    fun `GIVEN an initial state WHEN submit category THEN assert error is not thrown`() = runTest {
        // WHEN
        delegate.submitCategory()

        // THEN
        assertTrue(errorHandler.error is IllegalStateException)
    }

    private inner class ReceiverViewModel : ViewModel() {
        fun showSheet(categorySheetState: CategorySheetState) {
            delegate.showSheet(categorySheetState)
        }
    }
}
