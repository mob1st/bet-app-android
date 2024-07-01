package br.com.mob1st.features.finances.impl.ui.builder

import androidx.compose.runtime.Immutable
import arrow.optics.copy
import arrow.optics.optics
import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.core.kotlinx.errors.checkIs
import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.NotEnoughInputsException
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.features.utils.errors.toCommonError

/**
 * Consumable state for the category builder screen.
 * All the properties should be consumed by the UI layer to trigger the corresponding actions, setting them to null.
 * @property dialog The dialog that can be shown.
 * @property navTarget The navigation target that can be triggered.
 * @property snackbar The snackbar that can be shown.
 */
@optics
data class CategoryBuilderConsumables(
    val dialog: CategoryBuilderDialog? = null,
    val navTarget: CategoryBuilderNavTarget? = null,
    val snackbar: CategoryBuilderSnackbar? = null,
) {
    /**
     * Handles the given [throwable] error.
     * It sets the snackbar to show the error message.
     * If the error is a [NotEnoughInputsException], it will show a snackbar indicating the remaining inputs to be
     * added. Otherwise, it will show a generic error message.
     * @param throwable The error.
     * @return The next consumable state.
     */
    fun handleError(throwable: Throwable) = copy {
        CategoryBuilderConsumables.snackbar set if (throwable is NotEnoughInputsException) {
            CategoryBuilderSnackbar.NotAllowedToProceed(throwable.remainingInputs)
        } else {
            CategoryBuilderSnackbar.Failure(throwable.toCommonError())
        }
    }

    /**
     * Indicates the next consumable state when an item is selected in the manual category section.
     * If the given [item] is a [AddCategoryListItem], it will open the dialog to enter the category name.
     * If the given [item] is a [ManualCategoryListItem], it will navigate to the category edition screen.
     * If the given [item] us a [SuggestionListItem], it will throw an error because a suggestion should never
     * be selected in this section.
     * @param item The selected item.
     * @return The next consumable state.
     * @throws IllegalArgumentException If the given [item] is a [SuggestionListItem].
     */
    fun selectManualItem(item: CategoryListItem) = copy {
        when (item) {
            AddCategoryListItem -> CategoryBuilderConsumables.dialog set CategoryBuilderDialog.EnterName()
            is ManualCategoryListItem -> {
                CategoryBuilderConsumables.navTarget set CategoryBuilderNavTarget.EditCategory(
                    item.category.id,
                )
            }

            is SuggestionListItem -> require(false) {
                "A suggestion should never be selected in the manual category section."
            }
        }
    }

    /**
     * Indicates the next consumable state when an item is selected in the user suggestions section.
     * @param item The selected item.
     * @return The next consumable state.
     */
    fun selectUserSuggestion(item: SuggestionListItem) = copy {
        CategoryBuilderConsumables.navTarget set if (item.suggestion.linkedCategory == null) {
            CategoryBuilderNavTarget.AddCategory(
                name = item.leading,
                linkedSuggestion = item.suggestion.id,
            )
        } else {
            CategoryBuilderNavTarget.EditCategory(category = item.suggestion.linkedCategory.id)
        }
    }

    /**
     * Indicates the next consumable state when the user types the category name.
     * It opens the dialog to enter the category name.
     * @param name The category name.
     * @return The next consumable state.
     */
    fun typeCategoryName(name: String) = copy {
        CategoryBuilderConsumables.dialog set CategoryBuilderDialog.EnterName(name)
    }

    /**
     * Indicates the next consumable state when the user submits the category name.
     * It navigates to the "Add category" screen with the given name and a null linked suggestion since it should only
     * be called for manually added categories.
     * @return The next consumable state.
     * @throws IllegalArgumentException If the dialog is not an [CategoryBuilderDialog.EnterName].
     */
    fun submitCategoryName() = copy {
        val dialog = checkIs<CategoryBuilderDialog.EnterName>(dialog)
        CategoryBuilderConsumables.navTarget set CategoryBuilderNavTarget.AddCategory(
            name = TextState(dialog.name),
            linkedSuggestion = null,
        )
        CategoryBuilderConsumables.nullableDialog set null
    }

    /**
     * Navigates to the next step of the category builder.
     * If the given [builderNextAction] is a [BuilderNextAction.Complete], it will navigate to the next screen.
     * If the given [builderNextAction] is a [BuilderNextAction.Step], it will navigate to the next step.
     * @param builderNextAction The next action to go to.
     * @return The next consumable state.
     */
    fun navigateToNext(builderNextAction: BuilderNextAction) = copy {
        val target = when (builderNextAction) {
            is BuilderNextAction.Complete -> CategoryBuilderNavTarget.BuilderCompletion
            is BuilderNextAction.Step -> CategoryBuilderNavTarget.NextStep(builderNextAction)
        }
        CategoryBuilderConsumables.navTarget set target
    }

    /**
     * For [optics]
     */
    companion object
}

/**
 * UI state for the category builder screen.
 */
@Immutable
sealed interface CategoryBuilderDialog {
    /**
     * Dialog to enter the category name.
     * @property name The entered name.
     */
    @Immutable
    data class EnterName(
        val name: String = "",
    ) : CategoryBuilderDialog {
        /**
         * Indicates if the submit button should be enabled.
         * Very short names are or very long names are not allowed.
         * The length should be between [MIN_NAME_LENGTH] and [MAX_NAME_LENGTH].
         */
        val isSubmitEnabled: Boolean = name.isNotBlank() && name.length in MIN_NAME_LENGTH..MAX_NAME_LENGTH
    }

    companion object {
        private const val MIN_NAME_LENGTH = 3
        private const val MAX_NAME_LENGTH = 50
    }
}

/**
 * Navigation target for the category builder screen.
 */
@Immutable
sealed interface CategoryBuilderNavTarget {
    /**
     * If the builder is not completed and the user wants to go to the next step, this target should be triggered.
     * @property step The next step to go to.
     */
    @Immutable
    data class NextStep(
        val step: BuilderNextAction.Step,
    ) : CategoryBuilderNavTarget

    /**
     * Allows the user to navigate to the bottom sheet to edit an existing category.
     * It should be called only for categories that already exist.
     * @property category The category to edit.
     */
    @Immutable
    data class EditCategory(
        val category: RowId,
    ) : CategoryBuilderNavTarget

    /**
     * Allows the user to navigate to the bottom sheet to add a new category.
     * It should be called only for categories that do not exist yet.
     * @property name The name of the new category.
     * @property linkedSuggestion The suggestion linked to the new category, if any.
     */
    @Immutable
    data class AddCategory(
        val name: TextState,
        val linkedSuggestion: RowId?,
    ) : CategoryBuilderNavTarget

    /**
     * Indicates that the builder is completed and the user should be navigated to the next screen.
     * This target should be triggered when there is no more input to be given and the user wants to finish the builder.
     */
    @Immutable
    data object BuilderCompletion : CategoryBuilderNavTarget
}

/**
 * Snackbar state for the category builder screen.
 */
@Immutable
sealed interface CategoryBuilderSnackbar {
    /**
     * Snackbar to show when there are not enough suggestions to proceed to the next builder step.
     * @property remaining The remaining number of suggestions to be added before allowing the user to proceed.
     */
    @Immutable
    data class NotAllowedToProceed(val remaining: Int) : CategoryBuilderSnackbar

    /**
     * Snackbar to show when an error occurs.
     * @property commonError The error that occurred.
     */
    @Immutable
    data class Failure(val commonError: CommonError) : CategoryBuilderSnackbar
}
