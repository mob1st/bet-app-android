package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategoryBatch
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategoryInput
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategorySuggestion
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList

/**
 * Ui state for the category builder
 * @property manuallyAddedSection The manually added categories.
 * @property suggestedSection The suggested categories.
 */
@Immutable
internal data class BuilderUiState(
    val categoryType: CategoryType,
    val manuallyAddedSection: ManualCategoryBuilderSection = ManualCategoryBuilderSection(),
    val suggestedSection: SuggestedCategoryBuilderSection = SuggestedCategoryBuilderSection(),
    val isSaving: Boolean = false,
) {
    /**
     * Converts the state visible on screen to a structure that can be parcelized.
     */
    fun toSavedState(): BuilderUserInput {
        return BuilderUserInput(
            manuallyAdded = manuallyAddedSection.toManualEntryList(),
            suggested = suggestedSection.toSuggestedEntryMap(),
        )
    }

    fun toBatch(): CategoryBatch {
        return CategoryBatch(
            categoryType = categoryType,
            inputs = manuallyAddedSection.categories.map { it.input } + suggestedSection.categories.map { it.input },
        )
    }

    /**
     * Creates a bottom sheet to update a manual added item placed in the given [position].
     */
    fun showUpdateManualSheet(position: Int) = CategorySheetState.updateManual(
        position = position,
        input = manuallyAddedSection.categories[position].input,
    )

    /**
     * Creates a bottom sheet ot update a suggested item placed in the given [position]
     */
    fun showUpdateSuggestedSheet(position: Int) = CategorySheetState.updateSuggestion(
        position = position,
        input = suggestedSection.categories[position].input,
    )

    fun showNewManualSheet(name: String) = CategorySheetState(
        operation = CategorySheetState.Operation.Add,
        input = CategoryInput(type = categoryType, name = name),
    )

    fun createManualSuggestionsSection(userInput: BuilderUserInput): ManualCategoryBuilderSection {
        return ManualCategoryBuilderSection(
            categories = userInput.manuallyAdded.map { entry ->
                val input = CategoryInput(type = categoryType, name = entry.name)
                BuilderListItemState(input = input)
            }.toPersistentList(),
        )
    }

    fun createSuggestedSection(
        suggestions: List<CategorySuggestion>,
        userInput: BuilderUserInput,
    ): SuggestedCategoryBuilderSection {
        return SuggestedCategoryBuilderSection(
            suggestions = suggestions.toImmutableList(),
            categories = suggestions.map { suggestion ->
                val entry = userInput
                    .suggested
                    .getOrDefault(suggestion.id, BuilderUserInput.Entry())
                val input = CategoryInput(
                    type = categoryType,
                    name = entry.name,
                    value = Money.fromOrDefault(entry.amount),
                    linkedSuggestion = suggestion,
                )
                BuilderListItemState(input = input)
            }.toPersistentList(),
        )
    }

    fun combine(
        newManualSection: ManualCategoryBuilderSection,
        newSuggestedSection: SuggestedCategoryBuilderSection,
        isSaving: Boolean,
    ): BuilderUiState {
        return copy(
            manuallyAddedSection = newManualSection,
            suggestedSection = newSuggestedSection,
            isSaving = isSaving,
        )
    }
}

/**
 * The category name dialog state.
 * @property text The text.
 * @property isSubmitEnabled If the submit button is enabled.
 */
@Immutable
internal data class CategoryNameDialogState(
    val text: String = "",
    val isSubmitEnabled: Boolean = false,
)

/**
 * Allows the number set for a categ
 *  ory.
 * @property operation The operation to be executed when the [CategorySheetState] is submitted.
 * @property input The data to be updated.
 */
@Immutable
internal data class CategorySheetState(
    val operation: Operation,
    val input: CategoryInput,
) {
    val name: TextState = input.localizedName()
    val amount: String = input.value.toString()

    /**
     * Represents the operation to be performed when the sheet is submitted.
     */
    sealed interface Operation {
        /**
         * Adds a new category.
         * It's always a manual category because suggested categories are not added manually.
         */
        data object Add : Operation

        /**
         * Updates a category.
         * @property position The position of the selected category in the list.
         * @property isSuggestion If the category is a suggestion.
         */
        data class Update(
            val position: Int,
            val isSuggestion: Boolean,
        ) : Operation
    }

    companion object {
        /**
         * Creates a category sheet to update a manual category.
         */
        fun updateManual(
            position: Int,
            input: CategoryInput,
        ) = CategorySheetState(
            operation = Operation.Update(position, isSuggestion = false),
            input = input,
        )

        /**
         * Creates a category sheet to update a suggestion.
         */
        fun updateSuggestion(
            position: Int,
            input: CategoryInput,
        ) = CategorySheetState(
            operation = Operation.Update(position, isSuggestion = true),
            input = input,
        )
    }
}
