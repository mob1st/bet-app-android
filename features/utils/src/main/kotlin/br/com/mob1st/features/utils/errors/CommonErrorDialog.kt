package br.com.mob1st.features.utils.errors

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import br.com.mob1st.features.utils.R

@Composable
fun CommonErrorDialog(
    commonError: CommonError?,
    onDismiss: () -> Unit,
    onClickTryLater: () -> Unit,
) {
    commonError ?: return
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.utils_commonerror_helper_unknown_title))
        },
        text = {
            Text(stringResource(R.string.utils_commonerror_helper_unknown_subtitle))
        },
        confirmButton = {
            TextButton(onClick = onClickTryLater) {
                Text(stringResource(R.string.utils_commonerror_helper_unknown_button))
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
        ),
    )
}
