package br.com.mob1st.features.finances.impl.ui.builder

import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.domain.usecases.ProceedBuilderUseCase
import br.com.mob1st.features.finances.impl.domain.values.budgetBuilder
import br.com.mob1st.features.finances.impl.domain.values.category
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepCategorySheet
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepConsumables
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNameDialog
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNotAllowedSnackbar
import br.com.mob1st.features.finances.impl.ui.category.components.item.CategorySectionItemState
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
        val step = Arb.bind<BudgetBuilderAction.Step>().next()
        val item = CategorySectionItemState(Arb.category().next())
        val builderStepConsumables = BuilderStepConsumables()

        // When
        val result = builderStepConsumables.selectItem(step, item)

        // Then
        assertEquals(
            BuilderStepConsumables(
                sheet = BuilderStepCategorySheet(
                    intent = GetCategoryIntent.Edit(
                        id = item.category.id,
                        name = "oo",
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
                sheet = BuilderStepCategorySheet(
                    intent = GetCategoryIntent.Create(
                        name = "Category Name",
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
}
