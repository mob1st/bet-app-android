package br.com.mob1st.features.finances.impl.ui.builder.steps

import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.pluralStringResource
import arrow.optics.copy
import arrow.optics.optics
import br.com.mob1st.core.design.organisms.snack.SnackbarState
import br.com.mob1st.core.design.organisms.snack.snackbar
import br.com.mob1st.core.kotlinx.errors.checkIs
import br.com.mob1st.features.finances.impl.R
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.NotEnoughInputsException
import br.com.mob1st.features.finances.impl.ui.utils.components.CategorySectionItemState
import br.com.mob1st.features.utils.errors.CommonErrorSnackbarState

/**
 * Consumable state for the category builder screen.
 * All the properties should be consumed by the UI layer to trigger the corresponding actions, setting them to null.
 * @property dialog The dialog that can be shown.
 * @property navEvent The navigation target that can be triggered.
 * @property snackbar The snackbar that can be shown.
 */
@optics
data class BudgetBuilderStepConsumables(
    val dialog: BudgetBuilderStepDialog? = null,
    val navEvent: BudgetBuilderStepNavEvent? = null,
    val snackbar: SnackbarState? = null,
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
        BudgetBuilderStepConsumables.snackbar set if (throwable is NotEnoughInputsException) {
            BudgetBuilderStepSnackbar.NotAllowedToProceed(throwable.remainingInputs)
        } else {
            CommonErrorSnackbarState(throwable)
        }
    }

    /**
     * Indicates the next consumable state when an item is selected.
     * @param item The selected item.
     * @return The next consumable state.
     */
    fun selectItem(item: CategorySectionItemState) = copy {
        BudgetBuilderStepConsumables.navEvent set BudgetBuilderStepNavEvent.EditBudgetCategory(
            item.category.id,
        )
    }

    /**
     * Indicates the next consumable state when the user types the category name.
     * It opens the dialog to enter the category name.
     * @param name The category name.
     * @return The next consumable state.
     */
    fun typeCategoryName(name: String) = copy {
        BudgetBuilderStepConsumables.dialog set BudgetBuilderStepDialog.EnterName(name)
    }

    /**
     * Indicates the next consumable state when the user submits the category name.
     * It navigates to the "Add category" screen with the given name and a null linked suggestion since it should only
     * be called for manually added categories.
     * @return The next consumable state.
     * @throws IllegalArgumentException If the dialog is not an [BudgetBuilderStepDialog.EnterName].
     */
    fun submitCategoryName() = copy {
        val dialog = checkIs<BudgetBuilderStepDialog.EnterName>(dialog)
        BudgetBuilderStepConsumables.navEvent set BudgetBuilderStepNavEvent.AddBudgetCategory(
            name = dialog.name,
        )
        BudgetBuilderStepConsumables.nullableDialog set null
    }

    /**
     * Navigates to the next step of the category builder.
     * If the given [builderNextAction] is a [BuilderNextAction.Complete], it will navigate to the next screen.
     * If the given [builderNextAction] is a [BuilderNextAction.Step], it will navigate to the next step.
     * @param builderNextAction The next action to go to.
     * @return The next consumable state.
     */
    fun navigateToNext(builderNextAction: BuilderNextAction) = copy {
        BudgetBuilderStepConsumables.navEvent set BudgetBuilderStepNavEvent.NextAction(builderNextAction)
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
sealed interface BudgetBuilderStepDialog {
    /**
     * Dialog to enter the category name.
     * @property name The entered name.
     */
    @Immutable
    data class EnterName(
        val name: String = "",
    ) : BudgetBuilderStepDialog {
        /**
         * Indicates if the submit button should be enabled.
         * Very short names are or very long names are not allowed.
         */
        val isSubmitEnabled: Boolean = name.isNotBlank() && name.length >= MIN_NAME_LENGTH
    }

    companion object {
        private const val MIN_NAME_LENGTH = 3
    }
}

/**
 * Navigation target for the category builder screen.
 */
@Immutable
sealed interface BudgetBuilderStepNavEvent {
    /**
     * If the builder is not completed and the user wants to go to the next step, this target should be triggered.
     * @property action The next step to go to.
     */
    @Immutable
    data class NextAction(
        val action: BuilderNextAction,
    ) : BudgetBuilderStepNavEvent

    /**
     * Allows the user to navigate to the bottom sheet to edit an existing category.
     * It should be called only for categories that already exist.
     * @property category The category to edit.
     */
    @Immutable
    data class EditBudgetCategory(
        val category: Category.Id,
    ) : BudgetBuilderStepNavEvent

    /**
     * Allows the user to navigate to the bottom sheet to add a new category.
     * It should be called only for categories that do not exist yet.
     * @property name The name of the new category.
     */
    @Immutable
    data class AddBudgetCategory(
        val name: String,
    ) : BudgetBuilderStepNavEvent
}

/**
 * Snackbar state for the category builder screen.
 */
@Immutable
sealed interface BudgetBuilderStepSnackbar : SnackbarState {
    /**
     * Snackbar to show when there are not enough suggestions to proceed to the next builder step.
     * @property remaining The remaining number of suggestions to be added before allowing the user to proceed.
     */
    @Immutable
    data class NotAllowedToProceed(val remaining: Int) : BudgetBuilderStepSnackbar {
        @Composable
        override fun resolve(): SnackbarVisuals {
            val text = pluralStringResource(
                id = R.plurals.finances_builder_not_enough_items_error,
                count = remaining,
                remaining,
            )
            return remember(text) {
                snackbar(message = text)
            }
        }
    }
}
