package br.com.mob1st.features.finances.impl.ui.builder

import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.fixtures.budgetBuilder
import br.com.mob1st.features.finances.impl.domain.fixtures.category
import br.com.mob1st.features.finances.impl.domain.usecases.ProceedBuilderUseCase
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderCompleteNavEvent
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepCategoryDetailNavEvent
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepConsumables
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNameDialog
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNextNavEvent
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNotAllowedSnackbar
import br.com.mob1st.features.finances.impl.ui.category.components.item.CategorySectionItemState
import br.com.mob1st.features.finances.impl.ui.category.detail.CategoryDetailArgs
import br.com.mob1st.features.finances.impl.ui.fixtures.builderStepToNavArgsMap
import br.com.mob1st.features.utils.errors.CommonErrorSnackbarState
import br.com.mob1st.features.utils.errors.toCommonError
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.next
import kotlin.test.Test
import kotlin.test.assertEquals

class BuilderStepConsumablesTest {
    @Test
    fun `GIVEN a throwable WHEN handle a error THEN set snackbar to show the error message`() {
        // Given
        val throwable = Throwable()
        val builderStepConsumables = BuilderStepConsumables()

        // When
        val result = builderStepConsumables.handleError(throwable)

        // Then
        assertEquals(
            BuilderStepConsumables(snackbar = CommonErrorSnackbarState(throwable.toCommonError())),
            result,
        )
    }

    @Test
    fun `GIVEN a NotEnoughInputsException WHEN handle a error THEN set snackbar to show the remaining inputs`() {
        // Given
        val throwable = ProceedBuilderUseCase.NotEnoughInputsException(1)
        val builderStepConsumables = BuilderStepConsumables()

        // When
        val result = builderStepConsumables.handleError(throwable)

        // Then
        assertEquals(
            BuilderStepConsumables(snackbar = BuilderStepNotAllowedSnackbar(1)),
            result,
        )
    }

    @Test
    fun `GIVEN an list item to edit category WHEN select a manual item THEN navigate to the category edition screen`() {
        // Given
        val item = CategorySectionItemState(Arb.category().next())
        val builderStepConsumables = BuilderStepConsumables()

        // When

        val result = builderStepConsumables.selectItem(item)
        // Then
        assertEquals(
            BuilderStepConsumables(
                navEvent = BuilderStepCategoryDetailNavEvent(
                    args = CategoryDetailArgs(
                        intent = CategoryDetailArgs.Intent.Edit(item.category.id.value),
                        name = item.category.name,
                        recurrenceType = item.category.recurrences.asType(),
                        isExpense = item.category.isExpense,
                    ),
                ),
            ),
            result,
        )
    }

    @Test
    fun `GIVEN a category name WHEN type a category name THEN update category name state`() {
        // Given
        val name = "Category Name"
        val builderStepConsumables = BuilderStepConsumables()

        // When
        val result = builderStepConsumables.typeCategoryName(name)

        // Then
        assertEquals(
            BuilderStepConsumables(dialog = BuilderStepNameDialog(name)),
            result,
        )
    }

    @Test
    fun `GIVEN a category name WHEN submit a category name THEN open the dialog to enter the category name`() {
        val builder = Arb.budgetBuilder().next()
        // Given
        val builderStepConsumables = BuilderStepConsumables(
            dialog = BuilderStepNameDialog("Category Name"),
        )

        // When
        val result = builderStepConsumables.submitCategoryName(builder.id)

        // Then
        assertEquals(
            BuilderStepConsumables(
                navEvent = BuilderStepCategoryDetailNavEvent(
                    args = CategoryDetailArgs(
                        intent = CategoryDetailArgs.Intent.Create,
                        name = "Category Name",
                        recurrenceType = builder.id.type,
                        isExpense = builder.id.isExpense,
                    ),
                ),
            ),
            result,
        )
    }

    @Test
    fun `WHEN show name dialog THEN ensure it happen`() {
        // Given
        val builderStepConsumables = BuilderStepConsumables()

        // When
        val result = builderStepConsumables.showCategoryNameDialog()

        // Then
        assertEquals(
            BuilderStepConsumables(dialog = BuilderStepNameDialog()),
            result,
        )
    }

    @Test
    fun `GIVEN a next step WHEN navigate to next THEN assert event is next step`() {
        val consumables = BuilderStepConsumables()
        val step = Arb.bind<BudgetBuilderAction.Step>().next()
        val actual = consumables.navigateToNext(
            action = step,
        )
        val expected = BuilderStepConsumables(
            navEvent = BuilderStepNextNavEvent(
                next = builderStepToNavArgsMap.getRightValue(step),
            ),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a complete builder WHEN navigate to next THEN assert event is complete`() {
        val consumables = BuilderStepConsumables()
        val actual = consumables.navigateToNext(
            action = BudgetBuilderAction.Complete,
        )
        val expected = BuilderStepConsumables(
            navEvent = BuilderCompleteNavEvent,
        )
        assertEquals(expected, actual)
    }
}
