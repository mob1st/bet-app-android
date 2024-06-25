package br.com.mob1st.core.design.organisms.sheet

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HelperBottomSheet(
    modifier: Modifier,
    titleContent: @Composable () -> Unit,
    descriptionContent: @Composable () -> Unit,
    positiveButton: @Composable () -> Unit,
    negativeButton: @Composable () -> Unit,
) {
    AlertDialog(onDismissRequest = { /*TODO*/ }, confirmButton = { /*TODO*/ })
}

@Composable
@Preview
fun HelperBottomSheetPreview() {
    HelperBottomSheet(
        modifier = Modifier,
        titleContent = {},
        descriptionContent = {},
        positiveButton = {},
        negativeButton = {},
    )
}
