package br.com.mob1st.core.design.atoms.colors.contrast

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.runtime.compositionLocalOf

/**
 * Get the [UiModeManager] service from the context or null if it is not available.
 */
internal val Context.uiModeManager: UiModeManager?
    get() = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager?

/**
 * Get the current contrast value of the device.
 * If the device does not support contrast settings, the standard contrast level is returned.
 */
internal fun Context.getCurrentUiContrast(): Float {
    return if (isContrastSettingsSupported()) {
        uiModeManager?.contrast ?: UiContrast.STANDARD.threshold
    } else {
        UiContrast.STANDARD.threshold
    }
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
internal fun isContrastSettingsSupported() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE

internal val LocalUiContrast = compositionLocalOf<UiContrast> {
    error("No local composition for LocalUiContrast")
}
