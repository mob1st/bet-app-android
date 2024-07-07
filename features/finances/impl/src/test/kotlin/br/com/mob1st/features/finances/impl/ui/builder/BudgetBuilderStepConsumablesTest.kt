package br.com.mob1st.features.finances.impl.ui.builder

import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.entities.NotEnoughInputsException
import br.com.mob1st.features.finances.impl.ui.builder.steps.AddCategoryListItem
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepConsumables
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepDialog
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepNavEvent
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepSnackbar
import br.com.mob1st.features.finances.impl.ui.builder.steps.ManualCategoryListItem
import br.com.mob1st.features.finances.impl.ui.builder.steps.SuggestionListItem
import br.com.mob1st.features.finances.impl.utils.moduleFixture
import br.com.mob1st.features.utils.errors.CommonErrorSnackbarState
import br.com.mob1st.features.utils.errors.toCommonError
import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
        val throwable = NotEnoughInputsException(1)
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
    fun `GIVEN an list item to add category WHEN select a manual item THEN open the dialog to enter the category name`() {
        // Given
        val item = AddCategoryListItem
        val budgetBuilderStepConsumables = BudgetBuilderStepConsumables()

        // When
        val result = budgetBuilderStepConsumables.selectManualItem(item)

        // Then
        assertEquals(
            BudgetBuilderStepConsumables(dialog = BudgetBuilderStepDialog.EnterName()),
            result,
        )
    }

    @Test
    fun `GIVEN an list item to edit category WHEN select a manual item THEN navigate to the category edition screen`() {
        // Given
        val item = ManualCategoryListItem(moduleFixture())
        val budgetBuilderStepConsumables = BudgetBuilderStepConsumables()

        // When
        val result = budgetBuilderStepConsumables.selectManualItem(item)

        // Then
        assertEquals(
            BudgetBuilderStepConsumables(navEvent = BudgetBuilderStepNavEvent.EditBudgetCategory(item.category.id)),
            result,
        )
    }

    @Test
    fun `GIVEN an list item suggestion WHEN select a manual item THEN throw an error`() {
        // Given
        val item = SuggestionListItem(moduleFixture())
        val budgetBuilderStepConsumables = BudgetBuilderStepConsumables()

        // Then
        assertFailsWith<IllegalArgumentException> {
            // When
            budgetBuilderStepConsumables.selectManualItem(item)
        }
    }

    @Test
    fun `GIVEN a suggestion list item WHEN select a user suggestion THEN navigate to add category`() {
        // Given
        val item = SuggestionListItem(moduleFixture())
        val budgetBuilderStepConsumables = BudgetBuilderStepConsumables()

        // When
        val result = budgetBuilderStepConsumables.selectUserSuggestion(item)

        // Then
        assertEquals(
            BudgetBuilderStepConsumables(
                navEvent = BudgetBuilderStepNavEvent.AddBudgetCategory(
                    name = item.leading,
                    linkedSuggestion = item.suggestion.id,
                ),
            ),
            result,
        )
    }

    @Test
    fun `GIVEN a suggestion list item with linked category WHEN select a user suggestion THEN navigate to edit category`() {
        // Given
        val categorySuggestion = moduleFixture<CategorySuggestion>().copy(
            linkedCategory = moduleFixture {
                nullabilityStrategy(NeverNullStrategy)
            },
        )
        val item = SuggestionListItem(categorySuggestion)
        val budgetBuilderStepConsumables = BudgetBuilderStepConsumables()

        // When
        val result = budgetBuilderStepConsumables.selectUserSuggestion(item)

        // Then
        assertEquals(
            BudgetBuilderStepConsumables(
                navEvent = BudgetBuilderStepNavEvent.EditBudgetCategory(
                    category = item.suggestion.linkedCategory!!.id,
                ),
            ),
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
                    name = TextState("Category Name"),
                    linkedSuggestion = null,
                ),
            ),
            result,
        )
    }

    @Test
    fun `GIVEN a next step WHEN navigate THEN navigate to the next step`() {
        // Given
        val budgetBuilderStepConsumables = BudgetBuilderStepConsumables()

        // When
        val step = moduleFixture<BuilderNextAction.Step>()
        val result = budgetBuilderStepConsumables.navigateToNext(step)

        // Then
        assertEquals(
            BudgetBuilderStepConsumables(navEvent = BudgetBuilderStepNavEvent.NextStep(step)),
            result,
        )
    }

    @Test
    fun `GIVEN a complete action WHEN navigate THEN navigate to the next screen`() {
        // Given
        val budgetBuilderStepConsumables = BudgetBuilderStepConsumables()

        // When
        val result = budgetBuilderStepConsumables.navigateToNext(BuilderNextAction.Complete)

        // Then
        assertEquals(
            BudgetBuilderStepConsumables(navEvent = BudgetBuilderStepNavEvent.BuilderCompletionStep),
            result,
        )
    }
}
