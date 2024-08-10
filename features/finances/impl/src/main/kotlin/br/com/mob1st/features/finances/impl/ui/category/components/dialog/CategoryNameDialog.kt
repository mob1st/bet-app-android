package br.com.mob1st.features.finances.impl.ui.category.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.com.mob1st.features.finances.impl.R

/**
 * Displays a dialog to input a category name.
 * @param state The state for the component.
 * @param onType The callback to be called when the user types.
 * @param onClickSubmit The callback to be called when the user clicks the submit button.
 * @param onDismiss The callback to be called when the user dismisses the dialog.
 * @see CategoryNameDialogState
 */
@Composable
fun CategoryNameDialog(
    state: CategoryNameDialogState,
    onType: (String) -> Unit,
    onClickSubmit: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.finances_builder_commons_dialog_title))
        },
        text = {
            OutlinedTextField(
                value = state.name,
                onValueChange = onType,
                label = {
                    Text(text = stringResource(id = R.string.finances_builder_commons_suggestions_dialog_input_hint))
                },
            )
        },
        confirmButton = {
            TextButton(enabled = state.isSubmitEnabled, onClick = onClickSubmit) {
                Text(text = stringResource(id = R.string.finances_builder_commons_dialog_submit_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = android.R.string.cancel))
            }
        },
    )
}
