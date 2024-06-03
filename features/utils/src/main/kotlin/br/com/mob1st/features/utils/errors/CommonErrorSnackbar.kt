package br.com.mob1st.features.utils.errors

import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import br.com.mob1st.core.design.organisms.snack.snackbar
import br.com.mob1st.features.utils.R

@Composable
fun CommonError.toSnackbarVisuals(): SnackbarVisuals {
    val context = LocalContext.current
    return remember(this) {
        snackbar(
            message = context.getString(R.string.utils_commonerror_snackbar_unknown_supporting),
        )
    }
}
