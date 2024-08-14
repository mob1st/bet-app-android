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
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.domain.usecases.ProceedBuilderUseCase
import br.com.mob1st.features.finances.impl.ui.category.components.dialog.CategoryNameDialogState
import br.com.mob1st.features.finances.impl.ui.category.components.item.CategorySectionItemState
import br.com.mob1st.features.utils.errors.CommonErrorSnackbarState

/**
 * Consumable state for the category builder screen.
 * All the properties should be consumed by the UI layer to trigger the corresponding actions, setting them to null.
 * @property dialog The dialog that can be shown.
 * @property navEvent The navigation target that can be triggered.
 * @property sheet The sheet that can be shown.
 * @property snackbar The snackbar that can be shown.
 */
@optics
data class BuilderStepConsumables(
    val dialog: Dialog? = null,
    val navEvent: NavEvent? = null,
    val sheet: Sheet? = null,
    val snackbar: SnackbarState? = null,
) {
    /**
     * Handles the given [throwable] error.
     * It sets the snackbar to show the error message.
     * If the error is a [ProceedBuilderUseCase.NotEnoughInputsException], it will show a snackbar indicating the
     * remaining inputs to be added. Otherwise, it will show a generic error message.
     * @param throwable The error.
     * @return The next consumable state.
     */
    fun handleError(throwable: Throwable) = copy {
        BuilderStepConsumables.snackbar set if (throwable is ProceedBuilderUseCase.NotEnoughInputsException) {
            BuilderStepNotAllowedSnackbar(throwable.remainingInputs)
        } else {
            CommonErrorSnackbarState(throwable)
        }
    }

    /**
     * Indicates the next consumable state when an item is selected.
     * @param item The selected item.
     * @return The next consumable state.
     */
    fun selectItem(
        step: BudgetBuilderAction.Step,
        item: CategorySectionItemState,
    ) = copy {
        BuilderStepConsumables.navEvent set BuilderStepCategoryDetailNavEvent(
            intent = GetCategoryIntent.Edit(
                id = item.category.id,
                name = item.category.name,
            ),
            step = step,
        )
    }

    /**
     * Shows the dialog to enter the category name.
     * It's the first of 4 steps to add a new category.
     * @return The next consumable state.
     */
    fun showCategoryNameDialog() = copy {
        BuilderStepConsumables.dialog set BuilderStepNameDialog()
    }

    /**
     * It updates the dialog to show the given category name.
     * It's the second of 4 steps to add a new category.
     * @param name The category name.
     * @return The next consumable state.
     */
    fun typeCategoryName(name: String) = copy {
        BuilderStepConsumables.dialog set BuilderStepNameDialog(name = name)
    }

    /**
     * Indicates the next consumable state when the user submits the category name.
     * It navigates to the "Add category" screen with the given name and a null linked suggestion since it should only
     * be called for manually added categories.
     * @return The next consumable state.
     * @throws IllegalArgumentException If the dialog is not an [BuilderStepNameDialog].
     */
    fun submitCategoryName(step: BudgetBuilderAction.Step) = copy {
        val dialog = checkIs<BuilderStepNameDialog>(dialog)
        BuilderStepConsumables.sheet set BuilderStepCategorySheet(
            intent = GetCategoryIntent.Create(
                name = dialog.state.name,
            ),
        )
        BuilderStepConsumables.nullableDialog set null
    }

    /**
     * UI state for the category builder screen.
     */
    sealed interface Dialog

    /**
     * UI state to display bottom sheets
     */
    sealed interface Sheet

    /**
     * Navigation event for step screen
     */
    sealed interface NavEvent

    /**
     * For [optics]
     */
    companion object
}

/**
 * Enters the category name to start the category creation process.
 * @property state The state of the category name dialog.
 */
@Immutable
internal data class BuilderStepNameDialog(
    val state: CategoryNameDialogState = CategoryNameDialogState(),
) : BuilderStepConsumables.Dialog {
    constructor(name: String) : this(state = CategoryNameDialogState(name = name))
}

/**
 * Opens the bottom sheet for the category detail screen.
 */
@Immutable
internal data class BuilderStepCategorySheet(
    val intent: GetCategoryIntent,
) : BuilderStepConsumables.Sheet

/**
 * Snackbar to show when there are not enough suggestions to proceed to the next builder step.
 * @property remaining The remaining number of suggestions to be added before allowing the user to proceed.
 */
@Immutable
internal data class BuilderStepNotAllowedSnackbar(val remaining: Int) : SnackbarState {
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

/**
 * Navigation event triggered when when the current step is submitted.
 * @property next The next step to navigate to.
 */
@Immutable
internal data class BuilderStepNextNavEvent(
    val next: BudgetBuilderAction,
) : BuilderStepConsumables.NavEvent

/**
 * Navigation event that opens the category detail screen.
 * @property intent The intent to get the category.
 * @property step The current step.
 */
@Immutable
internal data class BuilderStepCategoryDetailNavEvent(
    val intent: GetCategoryIntent,
    val step: BudgetBuilderAction.Step,
) : BuilderStepConsumables.NavEvent
