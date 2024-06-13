package br.com.mob1st.features.twocents.builder.impl.ui.builder

import app.cash.turbine.test
import br.com.mob1st.tests.featuresutils.FakeErrorHandler
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CategoryNameDialogDelegateTest {
    private lateinit var delegate: CategoryNameDialogDelegate
    private lateinit var errorHandler: FakeErrorHandler

    @BeforeEach
    fun setup() {
        errorHandler = FakeErrorHandler()
        delegate = CategoryNameDialogDelegate(errorHandler)
    }

    @Test
    fun `GIVEN a initial state WHEN collect THEN assert the initial state is null`() = runTest {
        // WHEN
        delegate.dialogOutput.test {
            // THEN
            assertNull(awaitItem())
        }
    }

    @Test
    fun `GIVEN a initial state WHEN set dialog THEN assert the dialog is shown`() = runTest {
        // WHEN
        delegate.showCategoryNameDialog()

        // THEN
        delegate.dialogOutput.test {
            assertEquals(
                CategoryNameDialogState(),
                awaitItem(),
            )
        }
    }

    @Test
    fun `GIVEN a initial state WHEN show dialog And set category name THEN assert the category name is set`() = runTest {
        // GIVEN
        val name = "Category Name"

        // WHEN
        delegate.showCategoryNameDialog()
        delegate.setCategoryName(name)

        // THEN
        delegate.dialogOutput.test {
            assertEquals(
                CategoryNameDialogState(text = name),
                awaitItem(),
            )
        }
    }

    @Test
    fun `GIVEN a initial state WHEN show dialog And set name And get name And submit THEN assert the category name is returned`() = runTest {
        // GIVEN
        val name = "Category Name"

        // WHEN
        delegate.showCategoryNameDialog()
        delegate.setCategoryName(name)

        // THEN
        assertEquals(name, delegate.getNameAndSubmit())
    }

    @Test
    fun `GIVEN a initial state WHEN show dialog And consume it THEN assert dialog is removed`() = runTest {
        // WHEN
        delegate.showCategoryNameDialog()
        delegate.consumeDialog()

        // THEN
        delegate.dialogOutput.test {
            assertNull(awaitItem())
        }
    }

    @Test
    fun `GIVEN a initial state WHEN set a name THEN assert error is caught`() = runTest {
        // GIVEN
        val name = "Category Name"

        // WHEN
        delegate.setCategoryName(name)

        assertTrue(errorHandler.error is IllegalStateException)
    }
}
