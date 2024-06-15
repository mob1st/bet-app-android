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

    /**
     * Converts the input data to a batch of categories to be saved.
     * @throws NotEnoughSuggestionsException If there are less than 3 suggestions with input, which is the minimum
     * required to proceed.
     */
    @Throws(NotEnoughSuggestionsException::class)
    fun toBatch(): CategoryBatch {
        var greaterThanZeroCount = 0
        val manualInputs = manuallyAddedSection.categories.map {
            if (it.input.value > Money.Zero) {
                greaterThanZeroCount++
            }
            it.input
        }
        val suggestedInputs = suggestedSection.categories.map {
            if (it.input.value > Money.Zero) {
                greaterThanZeroCount++
            }
            it.input
        }
        if (greaterThanZeroCount < MINIMUM_SUGGESTIONS) {
            throw NotEnoughSuggestionsException(suggestedInputs.size)
        }
        return CategoryBatch(
            categoryType = categoryType,
            inputs = manualInputs + suggestedInputs,
        )
    }

    /**
     * Creates a bottom sheet to update a manual added item placed in the given [position].
     */
    fun showUpdateManualSheet(position: Int) = CategorySheetState.update(
        position = position,
        input = manuallyAddedSection.categories[position].input,
    )

    /**
     * Creates a bottom sheet ot update a suggested item placed in the given [position]
     */
    fun showUpdateSuggestedSheet(position: Int) = CategorySheetState.update(
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
                val input = CategoryInput(
                    type = categoryType,
                    name = entry.name,
                    value = Money.fromOrDefault(entry.amount, Money.Zero),
                )
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

    companion object {
        private const val MINIMUM_SUGGESTIONS = 3
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
     * Copies the state with the amount set.
     * @param amount The amount to be set.
     * @return A new [CategorySheetState] with the amount set.
     */
    fun setAmount(amount: String) = copy(
        input = input.copy(value = Money.from(amount)),
    )

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
         */
        data class Update(val position: Int) : Operation
    }

    companion object {
        /**
         * Creates a category sheet to update a manual category.
         */
        fun update(
            position: Int,
            input: CategoryInput,
        ) = CategorySheetState(
            operation = Operation.Update(position),
            input = input,
        )
    }
}
