package br.com.mob1st.core.androidx.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetLayout(
    modifier: Modifier = Modifier,
    bottomSheetNavigator: BottomSheetNavigator,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        bottomSheetNavigator.sheetInitializer()
        content()
    }
    if (bottomSheetNavigator.sheetEnabled) {
        ModalBottomSheet(
            onDismissRequest = bottomSheetNavigator.onDismissRequest,
            sheetState = bottomSheetNavigator.sheetState,
            content = {
                Column(
                    modifier = Modifier.fillMaxSize(0.95f),
                ) {
                    bottomSheetNavigator.sheetContent(this)
                }
            },
        )
    }
}
