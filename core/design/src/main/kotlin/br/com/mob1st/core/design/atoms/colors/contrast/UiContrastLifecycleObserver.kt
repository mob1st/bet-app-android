package br.com.mob1st.core.design.atoms.colors.contrast

import android.app.UiModeManager
import android.app.UiModeManager.ContrastChangeListener
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.Executor

/**
 * Listen for changes in the contrast settings of the device.
 */
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
private class UiContrastLifecycleObserver(
    private val uiModeManager: UiModeManager,
    private val executor: Executor,
    private val onChange: (Float) -> Unit,
) : DefaultLifecycleObserver {
    private val listener = ContrastChangeListener { contrast ->
        onChange(contrast)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        uiModeManager.addContrastChangeListener(executor, listener)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        uiModeManager.removeContrastChangeListener(listener)
    }
}

/**
 * Create a [LifecycleObserver] that listens for changes in the contrast settings of the device.
 * The context is used to get the [UiModeManager] service.
 * @param onChange Callback that will be called when the contrast changes.
 */
internal fun Context.uiContrastLifecycleObserver(
    onChange: (Float) -> Unit,
): LifecycleObserver {
    if (isContrastSettingsSupported()) {
        val uim = uiModeManager
        if (uim != null) {
            return UiContrastLifecycleObserver(
                uiModeManager = uim,
                executor = mainExecutor,
                onChange = onChange,
            )
        }
    }
    return LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            onChange(UiContrast.STANDARD.threshold)
        }
    }
}
