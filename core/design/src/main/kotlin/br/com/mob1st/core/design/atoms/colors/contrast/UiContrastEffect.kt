package br.com.mob1st.core.design.atoms.colors.contrast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Listen for changes in the contrast settings of the device.
 * The contrast listener is tied to the lifecycle of the [LocalLifecycleOwner] and it is removed when the lifecycle is
 * destroyed.
 * @param onChange Callback that will be called when the contrast changes.
 */
@Composable
internal fun UiContrastEffect(
    onChange: (Float) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val updatedOnChange by rememberUpdatedState(onChange)
    DisposableEffect(lifecycleOwner) {
        val observer = context.uiContrastLifecycleObserver {
            updatedOnChange(it)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
