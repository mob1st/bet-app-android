package br.com.mob1st.features.finances.impl.ui.builder

import br.com.mob1st.features.finances.impl.domain.usecases.ProceedBuilderUseCase
import br.com.mob1st.features.finances.impl.domain.values.category
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepConsumables
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepDialog
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepNavEvent
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepSnackbar
import br.com.mob1st.features.finances.impl.ui.utils.components.CategorySectionItemState
import br.com.mob1st.features.utils.errors.CommonErrorSnackbarState
import br.com.mob1st.features.utils.errors.toCommonError
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import kotlin.test.Test
import kotlin.test.assertEquals

class BudgetBuilderStepConsumablesTest {
    @Test
    fun `GIVEN a throwable WHEN handle a error THEN set snackbar to show the error message`() {
        // Given
        val throwable = Throwable()
        val budgetBuilderStepConsumables = BudgetBuilderStepConsumables()

        // When
        val result = budgetBuilderStepConsumables.handleError(throwable)

        // Then
        assertEquals(
            BudgetBuilderStepConsumables(snackbar = CommonErrorSnackbarState(throwable.toCommonError())),
            result,
        )
    }

    @Test
    fun `GIVEN a NotEnoughInputsException WHEN handle a error THEN set snackbar to show the remaining inputs`() {
        // Given
        val throwable = ProceedBuilderUseCase.NotEnoughInputsException(1)
        val budgetBuilderStepConsumables = BudgetBuilderStepConsumables()

        // When
        val result = budgetBuilderStepConsumables.handleError(throwable)

        // Then
        assertEquals(
            BudgetBuilderStepConsumables(snackbar = BudgetBuilderStepSnackbar.NotAllowedToProceed(1)),
            result,
        )
    }

    @Test
    fun `GIVEN an list item to edit category WHEN select a manual item THEN navigate to the category edition screen`() {
        // Given
        val item = CategorySectionItemState(Arb.category().next())
        val budgetBuilderStepConsumables = BudgetBuilderStepConsumables()

        // When
        val result = budgetBuilderStepConsumables.selectItem(item)

        // Then
        assertEquals(
            BudgetBuilderStepConsumables(navEvent = BudgetBuilderStepNavEvent.EditBudgetCategory(item.category.id)),
            result,
        )
    }

    @Test
    fun `GIVEN a category name WHEN type a category name THEN update category name state`() {
        // Given
        val name = "Category Name"
        val budgetBuilderStepConsumables = BudgetBuilderStepConsumables()

        // When
        val result = budgetBuilderStepConsumables.typeCategoryName(name)

        // Then
        assertEquals(
            BudgetBuilderStepConsumables(dialog = BudgetBuilderStepDialog.EnterName(name)),
            result,
        )
    }

    @Test
    fun `GIVEN a category name WHEN submit a category name THEN open the dialog to enter the category name`() {
        // Given
        val budgetBuilderStepConsumables = BudgetBuilderStepConsumables(
            dialog = BudgetBuilderStepDialog.EnterName("Category Name"),
        )

        // When
        val result = budgetBuilderStepConsumables.submitCategoryName()

        // Then
        assertEquals(
            BudgetBuilderStepConsumables(
                navEvent = BudgetBuilderStepNavEvent.AddBudgetCategory(
                    name = "Category Name",
                ),
            ),
            result,
        )
    }

    @Test
    fun `WHEN show name dialog THEN ensure it happen`() {
        // Given
        val budgetBuilderStepConsumables = BudgetBuilderStepConsumables()

        // When
        val result = budgetBuilderStepConsumables.showCategoryNameDialog()

        // Then
        assertEquals(
            BudgetBuilderStepConsumables(dialog = BudgetBuilderStepDialog.EnterName()),
            result,
        )
    }
}
