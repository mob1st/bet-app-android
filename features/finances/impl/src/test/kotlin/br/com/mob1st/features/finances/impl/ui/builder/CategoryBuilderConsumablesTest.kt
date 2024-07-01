package br.com.mob1st.features.finances.impl.ui.builder

import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.CategoryBuilder
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.entities.NotEnoughInputsException
import br.com.mob1st.features.finances.impl.utils.moduleFixture
import br.com.mob1st.features.utils.errors.toCommonError
import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CategoryBuilderConsumablesTest {
    @Test
    fun `GIVEN a throwable WHEN handle a error THEN set snackbar to show the error message`() {
        // Given
        val throwable = Throwable()
        val categoryBuilderConsumables = CategoryBuilderConsumables()

        // When
        val result = categoryBuilderConsumables.handleError(throwable)

        // Then
        assertEquals(
            CategoryBuilderConsumables(snackbar = CategoryBuilderSnackbar.Failure(throwable.toCommonError())),
            result,
        )
    }

    @Test
    fun `GIVEN a NotEnoughInputsException WHEN handle a error THEN set snackbar to show the remaining inputs`() {
        // Given
        val throwable = NotEnoughInputsException(1)
        val categoryBuilderConsumables = CategoryBuilderConsumables()

        // When
        val result = categoryBuilderConsumables.handleError(throwable)

        // Then
        assertEquals(
            CategoryBuilderConsumables(snackbar = CategoryBuilderSnackbar.NotAllowedToProceed(1)),
            result,
        )
    }

    @Test
    fun `GIVEN an list item to add category WHEN select a manual item THEN open the dialog to enter the category name`() {
        // Given
        val item = AddCategoryListItem
        val categoryBuilderConsumables = CategoryBuilderConsumables()

        // When
        val result = categoryBuilderConsumables.selectManualItem(item)

        // Then
        assertEquals(
            CategoryBuilderConsumables(dialog = CategoryBuilderDialog.EnterName()),
            result,
        )
    }

    @Test
    fun `GIVEN an list item to edit category WHEN select a manual item THEN navigate to the category edition screen`() {
        // Given
        val item = ManualCategoryListItem(moduleFixture())
        val categoryBuilderConsumables = CategoryBuilderConsumables()

        // When
        val result = categoryBuilderConsumables.selectManualItem(item)

        // Then
        assertEquals(
            CategoryBuilderConsumables(navTarget = CategoryBuilderNavTarget.EditCategory(item.category.id)),
            result,
        )
    }

    @Test
    fun `GIVEN an list item suggestion WHEN select a manual item THEN throw an error`() {
        // Given
        val item = SuggestionListItem(moduleFixture())
        val categoryBuilderConsumables = CategoryBuilderConsumables()

        // Then
        assertFailsWith<IllegalArgumentException> {
            // When
            categoryBuilderConsumables.selectManualItem(item)
        }
    }

    @Test
    fun `GIVEN a suggestion list item WHEN select a user suggestion THEN navigate to add category`() {
        // Given
        val item = SuggestionListItem(moduleFixture())
        val categoryBuilderConsumables = CategoryBuilderConsumables()

        // When
        val result = categoryBuilderConsumables.selectUserSuggestion(item)

        // Then
        assertEquals(
            CategoryBuilderConsumables(
                navTarget = CategoryBuilderNavTarget.AddCategory(
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
        val categoryBuilderConsumables = CategoryBuilderConsumables()

        // When
        val result = categoryBuilderConsumables.selectUserSuggestion(item)

        // Then
        assertEquals(
            CategoryBuilderConsumables(
                navTarget = CategoryBuilderNavTarget.EditCategory(
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
        val categoryBuilderConsumables = CategoryBuilderConsumables()

        // When
        val result = categoryBuilderConsumables.typeCategoryName(name)

        // Then
        assertEquals(
            CategoryBuilderConsumables(dialog = CategoryBuilderDialog.EnterName(name)),
            result,
        )
    }

    @Test
    fun `GIVEN a category name WHEN submit a category name THEN open the dialog to enter the category name`() {
        // Given
        val categoryBuilderConsumables = CategoryBuilderConsumables(
            dialog = CategoryBuilderDialog.EnterName("Category Name"),
        )

        // When
        val result = categoryBuilderConsumables.submitCategoryName()

        // Then
        assertEquals(
            CategoryBuilderConsumables(
                navTarget = CategoryBuilderNavTarget.AddCategory(
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
        val categoryBuilderConsumables = CategoryBuilderConsumables()
        val categoryBuilder = moduleFixture<CategoryBuilder>().copy(
            manuallyAdded = listOf(moduleFixture()),
            suggestions = listOf(moduleFixture()),
        )

        // When
        val step = moduleFixture<BuilderNextAction.Step>()
        val result = categoryBuilderConsumables.navigateToNext(step)

        // Then
        assertEquals(
            CategoryBuilderConsumables(navTarget = CategoryBuilderNavTarget.NextStep(step)),
            result,
        )
    }

    @Test
    fun `GIVEN a complete action WHEN navigate THEN navigate to the next screen`() {
        // Given
        val categoryBuilderConsumables = CategoryBuilderConsumables()

        // When
        val result = categoryBuilderConsumables.navigateToNext(BuilderNextAction.Complete)

        // Then
        assertEquals(
            CategoryBuilderConsumables(navTarget = CategoryBuilderNavTarget.BuilderCompletion),
            result,
        )
    }
}
